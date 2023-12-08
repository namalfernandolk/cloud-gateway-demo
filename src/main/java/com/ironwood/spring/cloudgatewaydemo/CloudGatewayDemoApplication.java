package com.ironwood.spring.cloudgatewaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudGatewayDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayDemoApplication.class, args);
	}

}
