package com.poc.es.elasticsearchspringboot;
import java.time.Instant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ElasticsearchSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchSpringbootApplication.class, args);
		Instant end = Instant.now();
		System.out.println("start time " + end);
	}

}
