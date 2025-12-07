package com.poc.es.elasticsearchspringboot.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.poc.es.elasticsearchspringboot.model.Audit;
import com.poc.es.elasticsearchspringboot.model.Dump;
import com.poc.es.elasticsearchspringboot.model.DumpOperation;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
  @Bean
  public ConsumerFactory<String, Audit> auditConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "audit-group");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
    return new DefaultKafkaConsumerFactory<>(props,
        new StringDeserializer(), new JsonDeserializer<>(Audit.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Audit> kafkaAuditListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Audit> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(auditConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, Dump> dumpConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "dump-group");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
    return new DefaultKafkaConsumerFactory<>(props,
        new StringDeserializer(), new JsonDeserializer<>(Dump.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Dump> kafkaDumpListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Dump> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(dumpConsumerFactory());
    return factory;
  }
  @Bean
  public ConsumerFactory<String, DumpOperation> dumpOpConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "dump-group");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
    return new DefaultKafkaConsumerFactory<>(props,
        new StringDeserializer(), new JsonDeserializer<>(DumpOperation.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, DumpOperation> kafkaDumpOpListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, DumpOperation> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(dumpOpConsumerFactory());
    return factory;
  }

}
