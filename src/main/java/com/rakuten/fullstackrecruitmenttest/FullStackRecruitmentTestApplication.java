package com.rakuten.fullstackrecruitmenttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.rakuten.fullstackrecruitmenttest.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class FullStackRecruitmentTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullStackRecruitmentTestApplication.class, args);
	}
}
