package com.messageService.messgaeService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MessgaeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessgaeServiceApplication.class, args);
	}

}
