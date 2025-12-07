package com.poc.es.elasticsearchspringboot.service.impl;

import org.springframework.stereotype.Service;

import com.poc.es.elasticsearchspringboot.connector.AuditConnector;
import com.poc.es.elasticsearchspringboot.service.AuditService;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Audit;

import co.elastic.clients.json.JsonData;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditConnector auditConnector;

    @Override
    public Audit fetchAuditById(String id) throws RecordNotFoundException, IOException {
        return auditConnector.fetchAuditById(id);
    }
    @Override
    public List<Audit> fetchAudits() throws RecordNotFoundException, IOException {
        return auditConnector.fetchAudits();
    }

    @Override
    public String insertAudit(Audit audit) throws IOException {
        System.out.println("Inserting audit..");
        return auditConnector.insertAudit(audit);
    }

    @Override
    public boolean bulkInsertAudits(List<Audit> audits) throws IOException {
       return auditConnector.bulkInsertAudits(audits);
    }

    @Override
    public List<Audit> fetchAuditsWithMustQuery(Audit audit) throws IOException {
        return auditConnector.fetchAuditsWithMustQuery(audit);
    }

    @Override
    public List<Audit> fetchAuditsWithShouldQuery(Audit audit) throws IOException {
        return auditConnector.fetchAuditsWithShouldQuery(audit);
    }

    @Override
    public String deleteAuditById(String id) throws IOException {
        return auditConnector.deleteAuditById(id);
    }

    @Override
    public String updateAudit(Audit audit) throws IOException {
        return auditConnector.updateAudit(audit);
    }
    @Override
    public JsonData parseJSONFile(InputStream input) {
        return auditConnector.readJson(input);
    }
    @Override
    public boolean deleteAudits(List<String> idList) throws IOException {
        // TODO Auto-generated method stub
        // return null;
        return auditConnector.deleteAudits(idList);
    }
}
