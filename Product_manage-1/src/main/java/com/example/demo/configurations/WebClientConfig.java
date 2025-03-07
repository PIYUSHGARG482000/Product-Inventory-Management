package com.example.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webclient() {
		return WebClient.builder()
						.baseUrl("http://localhost:9090/api/inventory")
						.build();
	}
	
}
