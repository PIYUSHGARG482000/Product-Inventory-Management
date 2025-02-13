package com.example.demo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.models.ProductInventory;
import com.example.demo.models.Status;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.services.InventoryServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class InventoryServiceTest {

	@Mock
	private InventoryRepository InventoryRepo;
	
	@InjectMocks
	private InventoryServices InventoryService;
	
	private ProductInventory Inventory1;
	private ProductInventory Inventory2;
	private ProductInventory UpdatedInventory;
	
	@BeforeEach
	void setUp() {
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
		when(InventoryRepo.findAll()).thenReturn(Flux.just(Inventory1, Inventory2));
		
		Flux<ProductInventory> allInventory = InventoryService.getAllInventory();
		
		StepVerifier.create(allInventory)
					.expectNext(Inventory1)
					.expectNext(Inventory2)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).findAll();
	}
	
	@Test
	void getInventoryByStockId() {
		when(InventoryRepo.findById("1")).thenReturn(Mono.just(Inventory1));
		
		Mono<ProductInventory> InventoryByStockId = InventoryService.getInventoryByStockId("1");
		
		StepVerifier.create(InventoryByStockId)
					.expectNext(Inventory1)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).findById("1");
		
	}
	
	@Test
	void getInventoryByStockIdNotFoundTest() {
		when(InventoryRepo.findById("3")).thenReturn(Mono.empty());
		
		Mono<ProductInventory> product = InventoryService.getInventoryByStockId("3");
		
		StepVerifier.create(product)
							.expectNextCount(0)
							.verifyComplete();
		verify(InventoryRepo, times(1)).findById("3");
	}
	
	@Test
	void addInventory() {
		when(InventoryRepo.save(Inventory1)).thenReturn(Mono.just(Inventory1));
		
		Mono<ProductInventory> addedInventory = InventoryService.addToInventory(Inventory1);
		
		StepVerifier.create(addedInventory)
					.expectNext(Inventory1)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).save(Inventory1);
	}
	
	@Test
	void updateInventoryTest() {
		this.UpdatedInventory = new ProductInventory();
		UpdatedInventory.setProductId("1");
		UpdatedInventory.setStockCount(12);
		UpdatedInventory.setStatus(Status.IN_STOCK);
		UpdatedInventory.setStockId("1");
		when(InventoryRepo.findById("1")).thenReturn(Mono.just(Inventory1));
		when(InventoryRepo.save(UpdatedInventory)).thenReturn(Mono.just(UpdatedInventory));
		
		Mono<ProductInventory> updatedProduct = InventoryService.updateToInventory(UpdatedInventory,"1");
		
		StepVerifier.create(updatedProduct)
							.expectNext(this.UpdatedInventory)
							.verifyComplete();
		
		verify(InventoryRepo, times(1)).findById("1");
		verify(InventoryRepo, times(1)).save(this.UpdatedInventory);
	}
	
	@Test
	void updateInventoryStockIdNotFoundTest() {
		this.UpdatedInventory = new ProductInventory();
		UpdatedInventory.setProductId("1");
		UpdatedInventory.setStockCount(12);
		UpdatedInventory.setStatus(Status.IN_STOCK);
		UpdatedInventory.setStockId("3");
		when(InventoryRepo.findById("3")).thenReturn(Mono.empty());
				
		Mono<ProductInventory> updatedProduct = InventoryService.updateToInventory(UpdatedInventory,"3");
		
		StepVerifier.create(updatedProduct)
							.expectNextCount(0)
							.verifyComplete();
		
		verify(InventoryRepo, times(1)).findById("3");
	}
	
	@Test
	void delteInventoryTest() {
		when(InventoryRepo.findById("1")).thenReturn(Mono.just(Inventory1));
		when(InventoryRepo.deleteById("1")).thenReturn(Mono.empty());
		
		Mono<ProductInventory> deleteInventory = InventoryService.deleteFromInventory("1");
		
		StepVerifier.create(deleteInventory)
					.expectNext(Inventory1)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).findById("1");
		verify(InventoryRepo, times(1)).deleteById("1");
	}
	
	@Test
	void delteInventoryStockIdNotFoundTest() {
		when(InventoryRepo.findById("3")).thenReturn(Mono.empty());
		
		Mono<ProductInventory> deleteInventory = InventoryService.deleteFromInventory("3");
		
		StepVerifier.create(deleteInventory)
					.expectNextCount(0)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).findById("3");
	}
	
	
	@Test
	void getInventoryByProductIdTest() {
		when(InventoryRepo.findByProductId("1")).thenReturn(Mono.just(Inventory1));
		
		Mono<ProductInventory> InventoryByProductId = InventoryService.getInventoryByProductId("1");
		
		StepVerifier.create(InventoryByProductId)
					.expectNext(Inventory1)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).findByProductId("1");
	}
	
	@Test
	void deleteInventoryByProductIdTest() {
		when(InventoryRepo.deleteByProductId("1")).thenReturn(Mono.just(Inventory1));
		
		Mono<ProductInventory> InventoryByProductId = InventoryService.deleteByProductId("1");
		
		StepVerifier.create(InventoryByProductId)
					.expectNext(Inventory1)
					.verifyComplete();
		
		verify(InventoryRepo, times(1)).deleteByProductId("1");
	}
	
	
	@Test
	void updateInventoryByProductIdTest() {
		this.UpdatedInventory = new ProductInventory();
		UpdatedInventory.setProductId("1");
		UpdatedInventory.setStockCount(12);
		UpdatedInventory.setStatus(Status.IN_STOCK);
		UpdatedInventory.setStockId("1");
		when(InventoryRepo.findByProductId("1")).thenReturn(Mono.just(Inventory1));
		when(InventoryRepo.save(UpdatedInventory)).thenReturn(Mono.just(UpdatedInventory));
		
		Mono<ProductInventory> updatedProduct = InventoryService.updateByProductId(UpdatedInventory,"1");
		
		StepVerifier.create(updatedProduct)
							.expectNext(this.UpdatedInventory)
							.verifyComplete();
		
		verify(InventoryRepo, times(1)).findByProductId("1");
		verify(InventoryRepo, times(1)).save(this.UpdatedInventory);
	}
}