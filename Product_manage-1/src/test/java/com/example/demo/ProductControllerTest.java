package com.example.demo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.controller.ProductController;
import com.example.demo.models.ProdInventoryDTO;
import com.example.demo.models.Product;
import com.example.demo.services.ProductServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers=ProductController.class)
class ProductControllerTest {
	
	@MockBean
	private ProductServices services;
	
	@InjectMocks
	private ProductController controller;
	
	private Product product1;
	private Product product2;
	private ProdInventoryDTO inventory1;
	private ProdInventoryDTO inventory2;
	
	
	@Autowired
	private WebTestClient webtestclient;
	
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);

		product1 = new Product();
		product1.setId("1");
		product1.setName("IPhone13");
		product1.setDescription("IPhone 13 with 64GB Rom");
		product1.setPrice(13.34);
		inventory1 = new ProdInventoryDTO();
		inventory1.setProduct(product1);
		inventory1.setStockCount(40);
		inventory1.setStockStatus("IN_STOCK");
		
		product2 = new Product();
		product2.setId("2");
		product2.setName("HP ProBook");
		product2.setDescription("HP Probook Laptop with Intel I5");
		product2.setPrice(1332.34);
		inventory2 = new ProdInventoryDTO();
		inventory2.setProduct(product2);
		inventory2.setStockCount(35);
		inventory2.setStockStatus("IN_STOCK");
		
//		this.webtestclient = WebTestClient.bindToController(controller).build(); //Building webTestClient by providing controller as arrays
	}
	
	@Test
	void getProductByIdTest() {
		when(services.getProductById("1")).thenReturn(Mono.just(inventory1));
		
		webtestclient.get()
					.uri("/api/products/{id}",1)
					.exchange()
					.expectStatus().isOk()
					.expectBody(ProdInventoryDTO.class)
					.isEqualTo(inventory1);
		
		verify(services, times(1)).getProductById("1");
	}
	
	@Test
	void getAllProductTest() {
		//Mocking the service call for expected result
		when(services.getAllProduct()).thenReturn(Flux.just(inventory1, inventory2));
		
		webtestclient.get()    //WebTest client call to Controller
					.uri("/api/products")
					.exchange()
					.expectStatus().isOk()
					.expectBodyList(ProdInventoryDTO.class)
					.hasSize(2)
					.contains(inventory1,inventory2);
					
		verify(services, times(1)).getAllProduct();		
	}
		
	
	@Test
	void addProductTest() {
		Product product3 = new Product();
		ProdInventoryDTO inventory3 = new ProdInventoryDTO();
		product3.setId("3");
		product3.setName("IPhone 15");
		product3.setDescription("IPhone 15 with 128GB ROM");
		product3.setPrice(13.34);
		inventory3.setProduct(product3);
		inventory3.setStockCount(0);
		inventory3.setStockStatus("OUT_OF_STOCK");
		
		
		when(services.addProduct(inventory3)).thenReturn(Mono.just(product3));
		
		webtestclient.post()
					 .uri("/api/products/addproduct")
					 .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue(inventory3)
					 .exchange()
					 .expectStatus().isCreated()
					 .expectBody(Product.class)
					 .isEqualTo(product3);
		
		
		verify(services, times(1)).addProduct(inventory3);
	}
	
	@Test
	void updateProductTest() {
		Product product4 = new Product();
		ProdInventoryDTO inventory4 = new ProdInventoryDTO();
		product4.setId("1");
		product4.setDescription("IPhone 15 Pro 128 GB ROM");
		product4.setName("IPhone 15");
		product4.setPrice(15559.76);
		inventory4.setProduct(product4);
		inventory4.setStockCount(-2);
		inventory4.setStockStatus("AVAILABLE_SOON");
		
				
		when(services.updateProduct(inventory4, "1")).thenReturn(Mono.just(inventory4));
		
		webtestclient.put()
					 .uri("/api/products/update/{id}",1)
					 .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue(inventory4)
					 .exchange()
					 .expectStatus().isAccepted()
					 .expectBody(ProdInventoryDTO.class)
					 .isEqualTo(inventory4);
		
		verify(services, times(1)).updateProduct(inventory4, "1");
	}
	
	@Test
	void deleteByIdTest() {
		when(services.deleteProduct("1")).thenReturn(Mono.just(inventory1));
		
		webtestclient.delete()
					 .uri("/api/products/delete/{id}",1)
					 .exchange()
					 .expectStatus().isOk();
		
		verify(services, times(1)).deleteProduct("1");	 
	}
	
}