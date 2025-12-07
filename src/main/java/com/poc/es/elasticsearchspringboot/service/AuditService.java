package com.poc.es.elasticsearchspringboot.service;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Audit;

import co.elastic.clients.json.JsonData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List; 

public interface AuditService {
    public Audit fetchAuditById(String id) throws RecordNotFoundException, IOException;
    public List<Audit> fetchAudits() throws RecordNotFoundException, IOException;

    public String insertAudit(Audit audit) throws IOException;

    public boolean bulkInsertAudits(List<Audit> audits) throws IOException;

    public List<Audit> fetchAuditsWithMustQuery(Audit audit) throws IOException;
    public List<Audit> fetchAuditsWithShouldQuery(Audit audit) throws IOException;

    public String deleteAuditById(String id) throws IOException;
    public boolean deleteAudits(List<String> idList) throws IOException;

    public String updateAudit(Audit audit) throws IOException;

    public JsonData parseJSONFile(InputStream input);
}
