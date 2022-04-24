package com.sendiri.microservices.demoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class DemoprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoprojectApplication.class, args);
	}

}
