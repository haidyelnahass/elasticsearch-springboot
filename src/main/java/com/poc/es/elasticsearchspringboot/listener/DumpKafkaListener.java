package com.poc.es.elasticsearchspringboot.listener;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;

import com.google.gson.Gson;
import com.poc.es.elasticsearchspringboot.enums.OperationsEnum;
import com.poc.es.elasticsearchspringboot.model.Dump;
import com.poc.es.elasticsearchspringboot.model.DumpOperation;
import com.poc.es.elasticsearchspringboot.service.DumpService;

@Component
@KafkaListener(topics = "eai-dump", groupId = "dump-group", containerFactory = "kafkaDumpOpListenerContainerFactory")
public class DumpKafkaListener {
    
    @Autowired
    private DumpService dumpService;
    
    @RetryableTopic(
          attempts = "4",
          backoff = @Backoff(delay = 1000, multiplier = 2.0),
          autoCreateTopics = "false",
          topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaHandler
    public void handleConsoleInput(String stringToParse) {
        System.out.println("Recieved String " + stringToParse);
        Gson g = new Gson();
        Dump dump = g.fromJson(stringToParse, Dump.class);
        System.out.println("Received Dump information : " + dump.toString());
        try {
            dumpService.insertDump(dump);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @KafkaHandler()
    // public void handleApiInput(Dump dump) {
    //     System.out.println("Received Dump information");
    //     Instant start = Instant.now();
    //     // your code
    //     try {
    //         dumpService.insertDump(dump);
    //         Instant end = Instant.now();
    //         Duration timeElapsed = Duration.between(start, end);
    //         System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
    //         System.out.println("Time now: " + end);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    @RetryableTopic(
        attempts = "4",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        autoCreateTopics = "false",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaHandler()
    public void handleDumpOpInput(DumpOperation dumpOp) {
        System.out.println("Received Dump information 2");
        Instant start = Instant.now();
        // your code
        try {
            if(dumpOp.getOperation() == OperationsEnum.INSERT) {
                System.out.println("Am I Hereeee?");
                dumpService.insertDump(dumpOp.getDumpData());
            } else if (dumpOp.getOperation() == OperationsEnum.UPDATE) {
                System.out.println("Am I Here?");
                System.out.println(dumpOp);

                dumpService.updateDump(dumpOp);
            }
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
            System.out.println("Time now: " + end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DltHandler
    public void dlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
      System.out.println(in + " from " + topic);
    }
}
