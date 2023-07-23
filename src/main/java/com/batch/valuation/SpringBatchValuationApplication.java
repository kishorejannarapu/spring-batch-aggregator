package com.batch.valuation;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchValuationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchValuationApplication.class, args);
	}

}
