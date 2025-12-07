package com.poc.es.elasticsearchspringboot.connector;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.ScoreSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import jakarta.json.spi.JsonProvider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Dump;
import com.poc.es.elasticsearchspringboot.model.DumpOperation;
import com.poc.es.elasticsearchspringboot.model.DumpSearchRequest;
import com.poc.es.elasticsearchspringboot.model.SortItems;
import com.poc.es.elasticsearchspringboot.utils.QueryBuilderUtils;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.core.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class DumpConnector {
    @Value("test_dump12")
    private String index;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public String insertDump(Dump dump) throws IOException {
        IndexRequest<Dump> request = IndexRequest.of(i -> i.index(index)
                .document(dump));
        IndexResponse response = elasticsearchClient.index(request);
        System.out.println("After inserting dump...");
        System.out.println(response);
        return response.result().toString();
    }

    public boolean bulkInsertDumps(List<Dump> dumpList) throws IOException {
        BulkRequest.Builder builder = new BulkRequest.Builder();
        dumpList.stream().forEach(dump -> builder.operations(op -> op.index(i -> i.index(index)
                .id(String.valueOf(dump.get_id()))
                .document(dump))));
        BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
        return !bulkResponse.errors();
    }

    public Dump fetchDumpById(String id) throws RecordNotFoundException, IOException {
        GetResponse<Dump> response = elasticsearchClient.get(req -> req.index(index)
                .id(id), Dump.class);
        if (!response.found())
            throw new RecordNotFoundException("Dump with ID" + id + " not found!");
        Dump hit = new Dump(response.id(), response.source());
        return hit;
    }

    public List<Dump> fetchDumps() throws RecordNotFoundException, IOException {
        SearchResponse<Dump> dumpSearchResponse = elasticsearchClient.search(req -> req.index(index).size(100),
                Dump.class);

        return dumpSearchResponse.hits().hits().stream()
                .map(hit -> new Dump(hit.id(), hit.source()))
                .collect(Collectors.toList());

    }

    public List<Dump> fetchDumpsWithScrollQuery(Dump dump, String scrollId) throws IOException {
        if (scrollId.isEmpty()) {

            List<Query> queries = prepareQueryList(dump);
            SearchRequest searchRequest = SearchRequest.of(i -> i.index(index).size(10000)
                    .query(query -> query.bool(bool -> bool.must(queries))).scroll(Time.of(j -> j.time("1m"))));
            SearchResponse<Dump> searchResponse = elasticsearchClient.search(searchRequest, Dump.class);
            System.out.println(searchResponse);
            System.out.println(searchResponse.scrollId());
            List<Dump> ResultList = searchResponse.hits().hits().stream()
                    .map(Hit::source).collect(Collectors.toList());
            System.out.println(ResultList.size());
            return searchResponse.hits().hits().stream()
                    .map(Hit::source).collect(Collectors.toList());
        } else {

            ScrollRequest scrollRequest = ScrollRequest
                    .of(i -> i.scrollId(scrollId).scroll(Time.of(j -> j.time("1m"))));
            ScrollResponse<Dump> searchResponse = elasticsearchClient.scroll(scrollRequest, Dump.class);
            return searchResponse.hits().hits().stream()
                    .map(Hit::source).collect(Collectors.toList());
        }
    }

    public List<Dump> fetchDumpsWithMustQuery(Dump dump) throws IOException {
        System.out.print(dump);
        List<Query> queries = prepareQueryList(dump);
        MatchQuery queryMatch = new MatchQuery.Builder().field("md_msg_data").query("124b52f0-6b3a-11ed-8068-0a0b011c0000").build();
        System.out.println(queryMatch);
        SortOptions sortScore = new SortOptions.Builder().score(new ScoreSort.Builder().order(SortOrder.Desc).build()).build();
        SortOptions sortCreationTimestamp = new SortOptions.Builder().field(f -> f.field("md_creation_tmstmp").order(SortOrder.Desc)).build();
        SearchResponse<Dump> dumpSearchResponse = elasticsearchClient.search(req -> req.index(index)
                .size(dump.getSize())
                .query(query -> query.match(queryMatch)).sort(sortScore).sort(sortCreationTimestamp),
                Dump.class);

                // f -> f.field("md_msg_data").query("69333c60-6b3a-11ed-8a5d-0a0b011c0000"))

        // System.out.println(dumpSearchResponse);

        return dumpSearchResponse.hits().hits().stream()
                .map(Hit::source).collect(Collectors.toList());
    }
    public List<Dump> searchDumps(DumpSearchRequest dumpSearchRequest) throws IOException {
        // System.out.print(dump);
        // Map<String, String> queries = prepareQueryList(dump);
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
        searchRequestBuilder.index(index).size(10);
        List<Query> mustQuery = prepareQueryList(dumpSearchRequest.getMustQuery());
        System.out.println(mustQuery);
        List<Query> shouldQuery = prepareQueryList(dumpSearchRequest.getShouldQuery());
        SortItems[] sortItems = dumpSearchRequest.getSortQuery();
        if(mustQuery != null) {
            searchRequestBuilder.query(query -> query.bool(bool -> bool.must(mustQuery)));
        }
         if(shouldQuery != null) {
            searchRequestBuilder.query(query -> query.bool(bool -> bool.should(shouldQuery)));
         }
        Map<String,String> matchMap = prepareQueryMap(dumpSearchRequest.getMatchQuery());
        System.out.println(matchMap);
        MatchQuery.Builder queryMatchBuilder ;
        MatchQuery queryMatch;
        
        System.out.println("Am I here?");
        if(matchMap != null) {
            queryMatchBuilder = new MatchQuery.Builder();
            for (Map.Entry<String,String> entry : matchMap.entrySet()) {
                if(entry.getValue() != null) {
                    queryMatchBuilder.field(entry.getKey()).query(entry.getValue().toString());
                }
            }
            queryMatch = queryMatchBuilder.build();
            System.out.println("Query Match " + queryMatch);
            searchRequestBuilder.query(query -> query.match(queryMatch));
        }
        System.out.println("Am I done?");
        SortOptions.Builder sortOptionBuilder = new SortOptions.Builder();
        SortOptions sortOptions;
        SortOrder order = SortOrder.Asc;
        if(sortItems != null) {
            for(SortItems sortItem: sortItems) {
                if(sortItem.getOrder().equals("desc")) {
                    order = SortOrder.Desc;
                } else {
                    order = SortOrder.Asc;
                }
                System.out.println(order);
                if(sortItem.getName().equals("_score")) {
                    SortOptions sortScore = new SortOptions.Builder().score(new ScoreSort.Builder().order(SortOrder.Desc).build()).build();
                    searchRequestBuilder.sort(sortScore);
                } else {
                    final SortOrder sortOrder = order;
                    sortOptionBuilder.field(f -> f.field(sortItem.getName()).order(sortOrder));
                }
            }
            sortOptions = sortOptionBuilder.build();
            System.out.println(sortOptions);
            searchRequestBuilder.sort(sortOptions);
        }

        

        SearchResponse<Dump> dumpSearchResponse = elasticsearchClient.search(searchRequestBuilder.build(),
                Dump.class);

                // f -> f.field("md_msg_data").query("69333c60-6b3a-11ed-8a5d-0a0b011c0000"))

        // System.out.println(dumpSearchResponse);

        return dumpSearchResponse.hits().hits().stream()
                .map(Hit::source).collect(Collectors.toList());
    }

    public List<Dump> fetchDumpsWithShouldQuery(Dump dump) throws IOException {
        // List<Query> queries = prepareQueryList(dump);
        // SearchResponse<Dump> dumpSearchResponse = elasticsearchClient.search(req -> req.index(index)
        //         .size(dump.getSize())
        //         .query(query -> query.bool(bool -> bool.should(queries))),
        //         Dump.class);

        // return dumpSearchResponse.hits().hits().stream()
        //         .map(Hit::source).collect(Collectors.toList());
        return null;
    }

    public String deleteDumpById(String id) throws IOException {
        System.out.println("Hello!");
        DeleteRequest request = DeleteRequest.of(req -> req.index(index).id(id));
        DeleteResponse response = elasticsearchClient.delete(request);
        return response.result().toString();
    }

    public boolean deleteDumps(List<String> idList) throws IOException {
        idList.forEach(id -> {
            try {
                DeleteRequest request = DeleteRequest.of(req -> req.index(index).id(id));
                DeleteResponse response = elasticsearchClient.delete(request);
            } catch (ElasticsearchException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        return false;
    }

    public String updateDump(Dump dump) throws IOException {
        UpdateRequest<Dump, Dump> updateRequest = UpdateRequest.of(req -> req.index(index)
                .id(String.valueOf(dump.get_id()))
                .doc(dump));
        UpdateResponse<Dump> response = elasticsearchClient.update(updateRequest, Dump.class);
        System.out.println("After updating dump...");
        System.out.println(response);
        return response.result().toString();
    }
    public String updateDump(Dump dump, String id) throws IOException {
        UpdateRequest<Dump, Dump> updateRequest = UpdateRequest.of(req -> req.index(index)
                .id(String.valueOf(id))
                .doc(dump));
        UpdateResponse<Dump> response = elasticsearchClient.update(updateRequest, Dump.class);
        System.out.println("After updating dump...");
        System.out.println(response);
        return response.result().toString();
    }
    public String updateDump(DumpOperation dumpOp) throws IOException {
        // search by business key first
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
        searchRequestBuilder.index(index).size(1);
        Map<String,String> matchMap = prepareQueryMap(dumpOp.getQueryData());
        System.out.println(matchMap);
        MatchQuery.Builder queryMatchBuilder ;
        MatchQuery queryMatch;
        
        System.out.println("Am I here?");
        if(matchMap != null) {
            queryMatchBuilder = new MatchQuery.Builder();
            for (Map.Entry<String,String> entry : matchMap.entrySet()) {
                if(entry.getValue() != null) {
                    queryMatchBuilder.field(entry.getKey()).query(entry.getValue().toString());
                }
            }
            queryMatch = queryMatchBuilder.build();
            System.out.println("Query Match " + queryMatch);
            searchRequestBuilder.query(query -> query.match(queryMatch));
        } else {
            System.out.print("Match Query Cannot be null");
            return null;
        }

        SearchResponse<Dump> dumpSearchResponse = elasticsearchClient.search(searchRequestBuilder.build(),
                Dump.class);

                // f -> f.field("md_msg_data").query("69333c60-6b3a-11ed-8a5d-0a0b011c0000"))

        // System.out.println(dumpSearchResponse);

        Dump foundDump = dumpSearchResponse.hits().hits().stream()
        .map(hit -> new Dump(hit.id(), hit.source())).collect(Collectors.toList()).get(0);
        System.out.print(foundDump);
        // then update
        UpdateRequest<Dump, Dump> updateRequest = UpdateRequest.of(req -> req.index(index)
                .id(foundDump.get_id())
                .doc(dumpOp.getDumpData()));
        UpdateResponse<Dump> response = elasticsearchClient.update(updateRequest, Dump.class);
        System.out.println("After updating dump...");
        System.out.println(response);
        return response.result().toString();
    }

    private List<Query> prepareQueryList(Dump dump) {
        // Field[] matchFields = dump.getClass().getDeclaredFields();
        if(dump != null) {
            Map<String, String> conditionMap = new HashMap<>();
            conditionMap.put("md_msg_id.keyword", dump.getMd_msg_id());
            conditionMap.put("md_flow_id.keyword", dump.getMd_flow_id());
            conditionMap.put("md_msg_tp.keyword", dump.getMd_msg_tp());
            conditionMap.put("md_creation_tmstmp.keyword", dump.getMd_creation_tmstmp());
            conditionMap.put("md_chnl_id.keyword", dump.getMd_chnl_id());
            conditionMap.put("md_rltn_type.keyword", dump.getMd_rltn_type());
            conditionMap.put("md_rltn_val.keyword", dump.getMd_rltn_val());
            conditionMap.put("md_fun_id.keyword", dump.getMd_fun_id());
            conditionMap.put("md_msg_data", dump.getMd_msg_data());
            conditionMap.put("md_brkr_name.keyword", dump.getMd_brkr_name());
            conditionMap.put("md_bank_id.keyword", dump.getMd_bank_id());
            conditionMap.put("md_branch_id.keyword", dump.getMd_branch_id());
            conditionMap.put("md_terminal_id.keyword", dump.getMd_terminal_id());
            conditionMap.put("md_card_num.keyword", dump.getMd_card_num());
            conditionMap.put("md_terminal_desc.keyword", dump.getMd_terminal_desc());
            conditionMap.put("md_request_id.keyword", dump.getMd_request_id());
            conditionMap.put("md_service_id.keyword", dump.getMd_service_id());
            conditionMap.put("md_app_name.keyword", dump.getMd_app_name());
            conditionMap.put("md_srvr_name.keyword", dump.getMd_srvr_name());
            conditionMap.put("md_qmgr_name.keyword", dump.getMd_qmgr_name());
            conditionMap.put("md_eg_name.keyword", dump.getMd_eg_name());
            conditionMap.put("md_process_id.keyword", dump.getMd_process_id());
            conditionMap.put("md_session_id.keyword", dump.getMd_session_id());
            conditionMap.put("md_party_id.keyword", dump.getMd_party_id());
            conditionMap.put("md_msg_data1", dump.getMd_msg_data1());
            conditionMap.put("md_msg_data2", dump.getMd_msg_data2());
            conditionMap.put("md_msg_data3", dump.getMd_msg_data3());
            conditionMap.put("md_msg_data4", dump.getMd_msg_data4());
    
            return conditionMap.entrySet().stream()
                    .filter(entry->!ObjectUtils.isEmpty(entry.getValue()))
                    .map(entry->QueryBuilderUtils.termQuery(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
    private Map<String,String> prepareQueryMap(Dump dump) {
        // Field[] matchFields = dump.getClass().getDeclaredFields();
        if(dump != null) {
            Map<String, String> conditionMap = new HashMap<>();
            conditionMap.put("md_msg_id.keyword", dump.getMd_msg_id());
            conditionMap.put("md_flow_id.keyword", dump.getMd_flow_id());
            conditionMap.put("md_msg_tp.keyword", dump.getMd_msg_tp());
            conditionMap.put("md_creation_tmstmp.keyword", dump.getMd_creation_tmstmp());
            conditionMap.put("md_chnl_id.keyword", dump.getMd_chnl_id());
            conditionMap.put("md_rltn_type.keyword", dump.getMd_rltn_type());
            conditionMap.put("md_rltn_val.keyword", dump.getMd_rltn_val());
            conditionMap.put("md_fun_id.keyword", dump.getMd_fun_id());
            conditionMap.put("md_msg_data", dump.getMd_msg_data());
            conditionMap.put("md_brkr_name.keyword", dump.getMd_brkr_name());
            conditionMap.put("md_bank_id.keyword", dump.getMd_bank_id());
            conditionMap.put("md_branch_id.keyword", dump.getMd_branch_id());
            conditionMap.put("md_terminal_id.keyword", dump.getMd_terminal_id());
            conditionMap.put("md_card_num.keyword", dump.getMd_card_num());
            conditionMap.put("md_terminal_desc.keyword", dump.getMd_terminal_desc());
            conditionMap.put("md_request_id.keyword", dump.getMd_request_id());
            conditionMap.put("md_service_id.keyword", dump.getMd_service_id());
            conditionMap.put("md_app_name.keyword", dump.getMd_app_name());
            conditionMap.put("md_srvr_name.keyword", dump.getMd_srvr_name());
            conditionMap.put("md_qmgr_name.keyword", dump.getMd_qmgr_name());
            conditionMap.put("md_eg_name.keyword", dump.getMd_eg_name());
            conditionMap.put("md_process_id.keyword", dump.getMd_process_id());
            conditionMap.put("md_session_id.keyword", dump.getMd_session_id());
            conditionMap.put("md_party_id.keyword", dump.getMd_party_id());
            conditionMap.put("md_msg_data1", dump.getMd_msg_data1());
            conditionMap.put("md_msg_data2", dump.getMd_msg_data2());
            conditionMap.put("md_msg_data3", dump.getMd_msg_data3());
            conditionMap.put("md_msg_data4", dump.getMd_msg_data4());
    
            return conditionMap;
        } else {
            return null;
        }
    }

    // Parse JSON File Input
    public JsonData readJson(InputStream input) {
        JsonpMapper jsonpMapper = elasticsearchClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }
}
