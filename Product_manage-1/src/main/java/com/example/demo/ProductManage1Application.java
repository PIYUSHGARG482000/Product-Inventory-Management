package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductManage1Application {

	public static void main(String[] args) {
		SpringApplication.run(ProductManage1Application.class, args);
	}

}
