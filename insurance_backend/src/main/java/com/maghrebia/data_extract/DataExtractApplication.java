package com.maghrebia.data_extract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.maghrebia.data_extract")
public class DataExtractApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataExtractApplication.class, args);
	}

}
