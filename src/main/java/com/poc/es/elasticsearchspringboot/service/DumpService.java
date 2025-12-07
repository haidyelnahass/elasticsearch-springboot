package com.poc.es.elasticsearchspringboot.service;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Dump;
import com.poc.es.elasticsearchspringboot.model.DumpOperation;
import com.poc.es.elasticsearchspringboot.model.DumpSearchRequest;

import java.io.IOException;
import java.util.List;
public interface DumpService {

    public Dump fetchDumpById(String id) throws RecordNotFoundException, IOException;
    public List<Dump> fetchDumps() throws RecordNotFoundException, IOException;

    public String insertDump(Dump dump) throws IOException;

    public boolean bulkInsertDumps(List<Dump> dumps) throws IOException;

    public List<Dump> fetchDumpsWithMustQuery(Dump dump) throws IOException;
    public List<Dump> fetchDumpsWithShouldQuery(Dump dump) throws IOException;
    public List<Dump> fetchDumpsWithScrollQuery(Dump dump, String scrollId) throws IOException;
    public List<Dump> searchDumps(DumpSearchRequest dumpSearchRequest) throws IOException;

    public String deleteDumpById(String id) throws IOException;
    public boolean deleteDumps(List<String> idList) throws IOException;

    public String updateDump(Dump dump) throws IOException;
    public String updateDump(DumpOperation dump) throws IOException;
}
