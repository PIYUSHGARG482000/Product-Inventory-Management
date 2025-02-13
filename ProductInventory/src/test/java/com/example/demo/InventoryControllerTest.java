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

import com.example.demo.controllers.InventoryController;
import com.example.demo.models.ProductInventory;
import com.example.demo.models.Status;
import com.example.demo.services.InventoryServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers=InventoryController.class)
class InventoryControllerTest {

	@MockBean
	private InventoryServices InventoryService;
	
	@InjectMocks
	private InventoryController InventoryController;
	
	@Autowired
	private WebTestClient webtestclient;
	
	private ProductInventory Inventory1;
	private ProductInventory Inventory2;
	private ProductInventory UpdatedInventory;
	
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
		
		Inventory1 = new ProductInventory();
		Inventory1.setProductId("1");
		Inventory1.setStatus(Status.IN_STOCK);
		Inventory1.setStockCount(30);
		Inventory1.setStockId("1");
		
		Inventory2 = new ProductInventory();
		Inventory2.setProductId("2");
		Inventory2.setStatus(Status.OUT_OF_STOCK);
		Inventory2.setStockCount(0);
		Inventory2.setStockId("2");
	}

	@Test
	void getAllInventoryTest() {
		when(InventoryService.getAllInventory()).thenReturn(Flux.just(Inventory1, Inventory2));
		
		webtestclient.get()    //WebTest client call to Controller
					.uri("/api/inventory")
					.exchange()
					.expectStatus().isOk()
					.expectBodyList(ProductInventory.class)
					.hasSize(2)
					.contains(Inventory1,Inventory2);
		
		verify(InventoryService, times(1)).getAllInventory();
	}
	
	@Test
	void getInventoryByStockId() {
		when(InventoryService.getInventoryByStockId("1")).thenReturn(Mono.just(Inventory1));
		
		webtestclient.get()    //WebTest client call to Controller
					.uri("/api/inventory/{id}", 1)
					.exchange()
					.expectStatus().isOk()
					.expectBodyList(ProductInventory.class)
					.contains(Inventory1);
		
		verify(InventoryService, times(1)).getInventoryByStockId("1");
		
	}
	
	@Test
	void getInventoryByStockIdNotFoundTest() {
		when(InventoryService.getInventoryByStockId("3")).thenReturn(Mono.empty());
		
		webtestclient.get()    //WebTest client call to Controller
					.uri("/api/inventory/{id}", 3)
					.exchange()
					.expectStatus().isOk()
					.expectBodyList(ProductInventory.class)
					.hasSize(0);
		
		verify(InventoryService, times(1)).getInventoryByStockId("3");
	}
	
	@Test
	void addInventory() {
		when(InventoryService.addToInventory(Inventory1)).thenReturn(Mono.just(Inventory1));
		
		webtestclient.post()    //WebTest client call to Controller
					 .uri("/api/inventory/addInventory")
					 .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue(Inventory1)
					 .exchange()
					 .expectStatus().isCreated()
					 .expectBody(ProductInventory.class)
					 .isEqualTo(Inventory1);
		
		verify(InventoryService, times(1)).addToInventory(Inventory1);
	}
	
	@Test
	void updateInventoryTest() {
		this.UpdatedInventory = new ProductInventory();
		UpdatedInventory.setProductId("1");
		UpdatedInventory.setStockCount(12);
		UpdatedInventory.setStatus(Status.IN_STOCK);
		UpdatedInventory.setStockId("1");
		when(InventoryService.updateToInventory(UpdatedInventory, "1")).thenReturn(Mono.just(UpdatedInventory));
		
		webtestclient.put()
					 .uri("/api/inventory/update/{id}",1)
					 .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue(UpdatedInventory)
					 .exchange()
					 .expectStatus().isAccepted()
					 .expectBody(ProductInventory.class)
					 .isEqualTo(UpdatedInventory);
	
		verify(InventoryService, times(1)).updateToInventory(this.UpdatedInventory, "1");
	}
	
	@Test
	void updateInventoryStockIdNotFoundTest() {
		this.UpdatedInventory = new ProductInventory();
		UpdatedInventory.setProductId("3");
		UpdatedInventory.setStockCount(12);
		UpdatedInventory.setStatus(Status.IN_STOCK);
		UpdatedInventory.setStockId("3");
		when(InventoryService.updateToInventory(UpdatedInventory,"3")).thenReturn(Mono.empty());
				
		webtestclient.put()
					 .uri("/api/inventory/update/{id}",3)
					 .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue(UpdatedInventory)
					 .exchange()
					 .expectStatus().isAccepted()
					 .expectBody(ProductInventory.class)
					 .isEqualTo(null);
		
		
		verify(InventoryService, times(1)).updateToInventory(UpdatedInventory, "3");
	}
	
	@Test
	void delteInventoryTest() {
		when(InventoryService.deleteFromInventory("1")).thenReturn(Mono.just(Inventory1));
		
		webtestclient.delete()
					 .uri("/api/inventory/delete/{id}",1)
					 .exchange()
					 .expectStatus().isOk()
					 .expectBody(ProductInventory.class);
		
		verify(InventoryService, times(1)).deleteFromInventory("1");
	}
	
	@Test
	void delteInventoryStockIdNotFoundTest() {
		when(InventoryService.deleteFromInventory("3")).thenReturn(Mono.empty());
		
		webtestclient.delete()
					 .uri("/api/inventory/delete/{id}",3)
					 .exchange()
					 .expectStatus().isOk()
					 .equals(null);
		
		verify(InventoryService, times(1)).deleteFromInventory("3");
	}
	
	@Test
	void getInventoryByProductIdTest() {
		when(InventoryService.getInventoryByProductId("1")).thenReturn(Mono.just(Inventory1));
		
		webtestclient.get()    //WebTest client call to Controller
					.uri("/api/inventory/product/{id}", 1)
					.exchange()
					.expectStatus().isOk()
					.expectBodyList(ProductInventory.class)
					.contains(Inventory1);
		
		verify(InventoryService, times(1)).getInventoryByProductId("1");
	}
	
	@Test
	void deleteInventoryByProductIdTest() {
		when(InventoryService.deleteByProductId("1")).thenReturn(Mono.just(Inventory1));
				
		webtestclient.delete()
					 .uri("/api/inventory/product/delete/{id}",1)
					 .exchange()
					 .expectStatus().isAccepted()
					 .expectBody(ProductInventory.class)
					 .isEqualTo(Inventory1);
		
		verify(InventoryService, times(1)).deleteByProductId("1");
	}
	
	
	@Test
	void updateInventoryByProductIdTest() {
		this.UpdatedInventory = new ProductInventory();
		UpdatedInventory.setProductId("1");
		UpdatedInventory.setStockCount(12);
		UpdatedInventory.setStatus(Status.IN_STOCK);
		UpdatedInventory.setStockId("1");
		when(InventoryService.updateByProductId(UpdatedInventory, "1")).thenReturn(Mono.just(UpdatedInventory));

		webtestclient.put()
					 .uri("/api/inventory/product/update/{id}",1)
					 .contentType(MediaType.APPLICATION_JSON)
					 .bodyValue(UpdatedInventory)
					 .exchange()
					 .expectStatus().isAccepted()
					 .expectBody(ProductInventory.class)
					 .isEqualTo(UpdatedInventory);
		
		verify(InventoryService, times(1)).updateByProductId(UpdatedInventory, "1");
	}

}