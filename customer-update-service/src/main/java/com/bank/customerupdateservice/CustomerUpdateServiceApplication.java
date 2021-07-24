package com.bank.customerupdateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerUpdateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerUpdateServiceApplication.class, args);
	}

}
