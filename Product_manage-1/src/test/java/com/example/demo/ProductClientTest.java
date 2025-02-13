package com.example.demo;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.configurations.ExternalClient;
import com.example.demo.models.ProductInventoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class inventoryClientTest {
	
	private static MockWebServer mockwebserver;
	private static ExternalClient inventoryClient;
	private static ProductInventoryDTO inventoryDTO1;
	private static ProductInventoryDTO inventoryDTO2;
	private static ProductInventoryDTO updatedInventory;
	

	@BeforeAll
    static void setUp() throws IOException {
        mockwebserver = new MockWebServer();
        mockwebserver.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockwebserver.shutdown();
    }

    @BeforeEach
    void initialize() {
    	String baseUrl = "http://localhost:%s".formatted(
                mockwebserver.getPort());
    	
    	inventoryClient = new ExternalClient(WebClient.builder().baseUrl(baseUrl).build());
    	inventoryDTO1 = new ProductInventoryDTO("100",12,"IN_STOCK", "1");
    	inventoryDTO2 = new ProductInventoryDTO("200", 0, "OUT_OF_STOCK", "2");
    } 
	
    @Test
    void getInventoryByProductId() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();
        
        mockwebserver.enqueue(new MockResponse()
          .setBody(objMapper.writeValueAsString(inventoryDTO1))
          .addHeader("Content-Type", "application/json"));

        Mono<ProductInventoryDTO> inventoryMono = inventoryClient.getInventoryByProductId("1");

        StepVerifier.create(inventoryMono)
          .expectNextMatches(inventory -> inventory.getProductId()
            .equals("1"))
          .verifyComplete();
    }
    
    @Test
    void addInventory() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();
        
        mockwebserver.enqueue(new MockResponse()
          .setBody(objMapper.writeValueAsString(inventoryDTO1))
          .addHeader("Content-Type", "application/json"));

        Mono<ProductInventoryDTO> inventoryMono = inventoryClient.addInventory(inventoryDTO1);

        StepVerifier.create(inventoryMono)
          .expectNextMatches(inventory -> 
          		inventory.getProductId().equals("1"))
          .verifyComplete();
    }
    
    @Test
    void updateInventory() throws Exception {
    	updatedInventory = new ProductInventoryDTO("100", 32, "IN_STOCK", "1");
        ObjectMapper objMapper = new ObjectMapper();
        
        mockwebserver.enqueue(new MockResponse()
          .setBody(objMapper.writeValueAsString(updatedInventory))
          .addHeader("Content-Type", "application/json"));

        Mono<ProductInventoryDTO> inventoryMono = inventoryClient.updateInventory(inventoryDTO1);

        StepVerifier.create(inventoryMono)
          .expectNextMatches(inventory -> 
          		inventory.getStockCount() == 32)
          .verifyComplete();
    }
    
    @Test
    void deleteInventory() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();
        
        mockwebserver.enqueue(new MockResponse()
          .setBody(objMapper.writeValueAsString(inventoryDTO1))
          .addHeader("Content-Type", "application/json"));

        Mono<ProductInventoryDTO> inventoryMono = inventoryClient.deleteInventory(inventoryDTO1.getProductId());

        StepVerifier.create(inventoryMono)
          .expectNextMatches(inventory -> 
          		inventory.getProductId().equals("1"))
          .verifyComplete();
    }
    
    @Test
    void getAllInventory() throws Exception{
    	 ObjectMapper objMapper = new ObjectMapper();
         
         mockwebserver.enqueue(new MockResponse()
           .setBody(objMapper.writeValueAsString(Arrays.asList(inventoryDTO1, inventoryDTO2)))
           .addHeader("Content-Type", "application/json"));

         Flux<ProductInventoryDTO> inventoryMono = inventoryClient.getAllInventory();

         StepVerifier.create(inventoryMono)
           .expectNext(inventoryDTO1)
           .expectNext(inventoryDTO2)
           .verifyComplete();
    }

}