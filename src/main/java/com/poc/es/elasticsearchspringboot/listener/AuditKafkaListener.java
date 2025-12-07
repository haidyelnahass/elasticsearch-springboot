package com.poc.es.elasticsearchspringboot.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.poc.es.elasticsearchspringboot.model.Audit;
import com.poc.es.elasticsearchspringboot.service.AuditService;


@Component
@KafkaListener(topics = "audit", groupId = "audit-group", containerFactory = "kafkaAuditListenerContainerFactory")
public class AuditKafkaListener {

    @Autowired
	private AuditService auditService;
    @KafkaHandler
    public void handleConsoleInput(String stringToParse) {
        System.out.println("Recieved String " + stringToParse);
        Gson g = new Gson(); 
        Audit audit = g.fromJson(stringToParse, Audit.class);  
        System.out.println("Received Audit information : " + audit.toString());
        try {
            auditService.insertAudit(audit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaHandler(isDefault = true)
    public void handleApiInput(Audit audit) {
        System.out.println("Received Audit information : " + audit.toString());
        try {
            auditService.insertAudit(audit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
