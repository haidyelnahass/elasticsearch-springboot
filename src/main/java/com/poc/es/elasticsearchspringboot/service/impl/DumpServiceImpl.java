package com.poc.es.elasticsearchspringboot.service.impl;

import org.springframework.stereotype.Service;

import com.poc.es.elasticsearchspringboot.connector.DumpConnector;
import com.poc.es.elasticsearchspringboot.service.DumpService;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Dump;
import com.poc.es.elasticsearchspringboot.model.DumpOperation;
import com.poc.es.elasticsearchspringboot.model.DumpSearchRequest;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
@Service
public class DumpServiceImpl implements DumpService {
    @Autowired
    private DumpConnector dumpConnector;

    @Override
    public Dump fetchDumpById(String id) throws RecordNotFoundException, IOException {
        return dumpConnector.fetchDumpById(id);
    }
    @Override
    public List<Dump> fetchDumps() throws RecordNotFoundException, IOException {
        return dumpConnector.fetchDumps();
    }

    @Override
    public String insertDump(Dump dump) throws IOException {
        return dumpConnector.insertDump(dump);
    }

    @Override
    public boolean bulkInsertDumps(List<Dump> dumps) throws IOException {
       return dumpConnector.bulkInsertDumps(dumps);
    }

    @Override
    public List<Dump> fetchDumpsWithMustQuery(Dump dump) throws IOException {
        return dumpConnector.fetchDumpsWithMustQuery(dump);
    }

    @Override
    public List<Dump> fetchDumpsWithShouldQuery(Dump dump) throws IOException {
        return dumpConnector.fetchDumpsWithShouldQuery(dump);
    }
    @Override
    public List<Dump> fetchDumpsWithScrollQuery(Dump dump, String scrollId) throws IOException {
        return dumpConnector.fetchDumpsWithScrollQuery(dump, scrollId);
    }

    @Override
    public String deleteDumpById(String id) throws IOException {
        return dumpConnector.deleteDumpById(id);
    }

    @Override
    public String updateDump(Dump dump) throws IOException {
        return dumpConnector.updateDump(dump);
    }
    @Override
    public String updateDump(DumpOperation dump) throws IOException {
        return dumpConnector.updateDump(dump);
    }
    @Override
    public boolean deleteDumps(List<String> idList) throws IOException {
        // TODO Auto-generated method stub
        // return null;
        return dumpConnector.deleteDumps(idList);
    }
    @Override
    public List<Dump> searchDumps(DumpSearchRequest dumpSearchRequest) throws IOException {
        // TODO Auto-generated method stub
        return dumpConnector.searchDumps(dumpSearchRequest);
    }
}
