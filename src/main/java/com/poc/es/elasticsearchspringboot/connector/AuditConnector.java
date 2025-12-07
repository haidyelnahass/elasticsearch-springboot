package com.poc.es.elasticsearchspringboot.connector;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import jakarta.json.spi.JsonProvider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Audit;
import com.poc.es.elasticsearchspringboot.utils.QueryBuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditConnector {
    @Value("audit")
    private String index;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public String insertAudit(Audit audit) throws IOException {
        IndexRequest<Audit> request = IndexRequest.of(i->
                i.index(index)
                        .document(audit));
        IndexResponse response = elasticsearchClient.index(request);
        System.out.println("After inserting audit...");
        System.out.println(response);
        return response.result().toString();
    }

    public boolean bulkInsertAudits(List<Audit> auditList) throws IOException {
        BulkRequest.Builder builder = new BulkRequest.Builder();
        auditList.stream().forEach(audit ->
            builder.operations(op->
                    op.index(i->
                            i.index(index)
                                    .id(String.valueOf(audit.getId()))
                                    .document(audit)))
        );
        BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
        return !bulkResponse.errors();
    }
    public boolean deleteAudits(List<String> idList) throws IOException {
        // BulkRequest.Builder builder = new BulkRequest.Builder();
        // idList.stream().forEach(audit ->
        //     builder.operations(op->
        //             op.index(i->
        //                     i.index(index)
        //                             .id(audit).document(null)
        //                             ))
        // );
        // BulkResponse bulkResponse = elasticsearchClient.bulk(builder.build());
        // return !bulkResponse.errors();
        return false;
    }

    public Audit fetchAuditById(String id) throws RecordNotFoundException, IOException {
        GetResponse<Audit> response = elasticsearchClient.get(req->
                req.index(index)
                        .id(id),Audit.class);
        if(!response.found())
            throw new RecordNotFoundException("Audit with ID" + id + " not found!");
        Audit hit = new Audit(response.id(),response.source());
        return hit;
    }
    public List<Audit> fetchAudits() throws RecordNotFoundException, IOException {
        SearchResponse<Audit> auditSearchResponse = elasticsearchClient.search(req->
                req.index(index).size(50),
                Audit.class);

        return auditSearchResponse.hits().hits().stream()
                .map(hit -> 
                        new Audit(hit.id(),hit.source())
                )
                .collect(Collectors.toList());

        
    }

    public List<Audit> fetchAuditsWithMustQuery(Audit audit) throws IOException {
        List<Query> queries = prepareQueryList(audit);
        
        SearchResponse<Audit> auditSearchResponse = elasticsearchClient.search(req->
                req.index(index)
                        .size(audit.getSize())
                        .query(query->
                                query.bool(bool->
                                        bool.must(queries))),
                Audit.class);

        return auditSearchResponse.hits().hits().stream()
                .map(Hit::source).collect(Collectors.toList());
    }

    public List<Audit> fetchAuditsWithShouldQuery(Audit audit) throws IOException {
        List<Query> queries = prepareQueryList(audit);
        SearchResponse<Audit> auditSearchResponse = elasticsearchClient.search(req->
                        req.index(index)
                                .size(audit.getSize())
                                .query(query->
                                        query.bool(bool->
                                                bool.should(queries))),
                Audit.class);

        return auditSearchResponse.hits().hits().stream()
                .map(Hit::source).collect(Collectors.toList());
    }

    public String deleteAuditById(String id) throws IOException {
        System.out.println("Hello!");
        DeleteRequest request = DeleteRequest.of(req->
                req.index(index).id(id));
        DeleteResponse response = elasticsearchClient.delete(request);
        return response.result().toString();
    }
    

    public String updateAudit(Audit audit) throws IOException {
        UpdateRequest<Audit, Audit> updateRequest = UpdateRequest.of(req->
                req.index(index)
                        .id(String.valueOf(audit.getId()))
                        .doc(audit));
        UpdateResponse<Audit> response = elasticsearchClient.update(updateRequest, Audit.class);
        return response.result().toString();
    }


    private List<Query> prepareQueryList(Audit audit) {
        Map<String, String> conditionMap = new HashMap<>();
        conditionMap.put("tenantID.keyword", audit.getTenantID());
        conditionMap.put("apiName.keyword", audit.getApiName());
        conditionMap.put("operationID.keyword", audit.getOperationID());
        conditionMap.put("categoryID.keyword", audit.getCategoryID());
        conditionMap.put("processID.keyword", audit.getProcessID());
        conditionMap.put("processName.keyword", audit.getProcessName());
        conditionMap.put("processNameAR.keyword", audit.getProcessNameAR());
        // conditionMap.put("activityID.keyword", audit.getActivityID());
        // conditionMap.put("activityName.keyword", audit.getActivityName());
        // conditionMap.put("activityNameAR.keyword", audit.getActivityNameAR());
        // conditionMap.put("requestID.keyword", audit.getRequestID());
        // conditionMap.put("userID.keyword", audit.getUserID());
        // conditionMap.put("username.keyword", audit.getUsername());
        // conditionMap.put("organizationID.keyword", audit.getOrganizationID());
        // conditionMap.put("status.keyword", audit.getStatus());
        // conditionMap.put("statusDescription.keyword", audit.getStatusDescription());
        // conditionMap.put("description.keyword", audit.getDescription());

        return conditionMap.entrySet().stream()
                .filter(entry->!ObjectUtils.isEmpty(entry.getValue()))
                .map(entry->QueryBuilderUtils.termQuery(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

//     Parse JSON File Input
    public  JsonData readJson(InputStream input) {
        JsonpMapper jsonpMapper = elasticsearchClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();
    
        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }
    
}
