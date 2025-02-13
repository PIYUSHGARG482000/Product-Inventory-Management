package com.example.demo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.demo.configurations.ExternalClient;
import com.example.demo.models.ProdInventoryDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductInventoryDTO;
import com.example.demo.repository.ProductRepo;
import com.example.demo.services.ProductServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ProductServiceTest {

	@Mock
	private ProductRepo repository; 
	
	@Mock
	private ExternalClient client;
	
	@InjectMocks
	private ProductServices services;
	
	
	private Product product1;
	private Product product2;
	private Product product3;
	private ProdInventoryDTO inventory1;
	private ProdInventoryDTO inventory2;
	private ProdInventoryDTO inventory3;
	private ProductInventoryDTO productInventoryDTO1;
	private ProductInventoryDTO productInventoryDTO2;
	private ProductInventoryDTO productInventoryDTO3;
	
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this); //Setting the Repository Mock for the Member Objects
		
		product1 = new Product();
		product1.setId("1");
		product1.setName("IPhone13");
		product1.setDescription("IPhone 13 with 64GB Rom");
		product1.setPrice(13.34);
		
		product2 = new Product();
		product2.setId("2");
		product2.setName("HP ProBook");
		product2.setDescription("HP Probook Laptop with Intel I5");
		product2.setPrice(1332.34);
		
		product3 = new Product();
		product3.setId("3");
		product3.setName("HP IdeaPad Laptop");
		product3.setDescription("HP IdeaPad Laptop with Intel I5");
		product3.setPrice(2332.34);
				
		inventory1 = ProdInventoryDTO.builder().product(product1).StockCount(1).StockStatus("IN_STOCK").build(); //new ProductWithInventoryDTO(product1,1,"IN_STOCK");
		inventory2 = ProdInventoryDTO.builder().product(product2).StockCount(0).StockStatus("OUT_OF_STOCK").build();//new ProductWithInventoryDTO(product2,0,"OUT_OF_STOCK");
		inventory3 = ProdInventoryDTO.builder().product(product2).StockCount(-1).StockStatus("AVAILABLE_SOON").build();//new ProductWithInventoryDTO(product2,0,"OUT_OF_STOCK");
		
		productInventoryDTO1 = new ProductInventoryDTO();
		productInventoryDTO1.setStockCount(1);
		productInventoryDTO1.setProductId("1");
		productInventoryDTO1.setStatus("IN_STOCK");
		productInventoryDTO1.setStockId("1");
		
		productInventoryDTO2 = new ProductInventoryDTO();
		productInventoryDTO2.setStockCount(0);
		productInventoryDTO2.setProductId("2");
		productInventoryDTO2.setStatus("OUT_OF_STOCK");
		productInventoryDTO2.setStockId("2");
		
		productInventoryDTO3 = new ProductInventoryDTO();
		productInventoryDTO3.setStockCount(-1);
		productInventoryDTO3.setProductId("3");
		productInventoryDTO3.setStatus("AVAILABLE_SOON");
		productInventoryDTO3.setStockId("3");
		
	}

	@Test
	void getAllProductTest() {
		//TestCase scenario
		when(repository.findAll()).thenReturn(Flux.just(product1,product2));
		when(client.getAllInventory()).thenReturn(Flux.just(productInventoryDTO1, productInventoryDTO2));
		
		Flux<ProdInventoryDTO> allproducts = services.getAllProduct();  //Service call
		
		StepVerifier.create(allproducts)  //Used to create TestCase scenario for Unit Test
					.expectNext(inventory1)
					.expectNext(inventory2)
					.verifyComplete();
		
		verify(repository, times(1)).findAll();	
		verify(client, times(1)).getAllInventory();
	}
	
	@Test
	void addProductTest_IN_STOCK() {
		when(repository.save(Mockito.any())).thenReturn(Mono.just(product1));
		when(client.addInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<Product> product = services.addProduct(inventory1);
		StepVerifier.create(product)
					.expectNext(product1)
					.verifyComplete();
		
		verify(repository, times(1)).save(Mockito.any());
		verify(client, times(1)).addInventory(Mockito.any());
 
	}
	@Test
	void addProductTest_OUT_OF_STOCK() {
		when(repository.save(Mockito.any())).thenReturn(Mono.just(product2));
		when(client.addInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO2));
		Mono<Product> product = services.addProduct(inventory2);
		StepVerifier.create(product)
					.expectNext(product2)
					.verifyComplete();
		
		verify(repository, times(1)).save(Mockito.any());
		verify(client, times(1)).addInventory(Mockito.any());
 
	}
	@Test
	void addProductTest_AVAILABLE_SOON() {
		when(repository.save(Mockito.any())).thenReturn(Mono.just(product3));
		when(client.addInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO3));
		Mono<Product> product = services.addProduct(inventory3);
		StepVerifier.create(product)
					.expectNext(product3)
					.verifyComplete();
		
		verify(repository, times(1)).save(Mockito.any());
		verify(client, times(1)).addInventory(Mockito.any());
 
	}
	
	@Test
	void getProductByIdTest() {
		//TestCase Scenario
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.just(product1));
		when(client.getInventoryByProductId(Mockito.anyString())).thenReturn(Mono.just(productInventoryDTO1));
		
		Mono<ProdInventoryDTO> product = services.getProductById("1"); //Service Call
			
		StepVerifier.create(product)  //Used to create TestCase scenario for Unit Test
							.expectNext(inventory1)
							.verifyComplete();
		
		verify(repository, times(1)).findById(Mockito.anyString());
	}
	
	@Test
	void updateProductTest_OnSuccess_IN_STOCK() {
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.just(product1));
		when(repository.save(Mockito.any())).thenReturn(Mono.just(product1));
		when(client.updateInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<ProdInventoryDTO> result = services.updateProduct(inventory1, "1");
		StepVerifier.create(result)
					.expectNext(inventory1)
					.verifyComplete();		
		
		verify(repository, times(1)).findById(Mockito.anyString());
		verify(repository, times(1)).save(Mockito.any());
		verify(client, times(1)).updateInventory(Mockito.any());
	}
	@Test
	void updateProductTest_OnSuccess_OUT_OF_STOCK() {
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.just(product2));
		when(repository.save(Mockito.any())).thenReturn(Mono.just(product2));
		when(client.updateInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO2));
		Mono<ProdInventoryDTO> result = services.updateProduct(inventory2, "2");
		StepVerifier.create(result)
					.expectNext(inventory2)
					.verifyComplete();
		
		verify(repository, times(1)).findById(Mockito.anyString());
		verify(repository, times(1)).save(Mockito.any());
		verify(client, times(1)).updateInventory(Mockito.any());
	}
	@Test
	void updateProductTest_OnSuccess_AVAILABLE_SOON() {
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.just(product3));
		when(repository.save(Mockito.any())).thenReturn(Mono.just(product3));
		when(client.updateInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO3));
		Mono<ProdInventoryDTO> result = services.updateProduct(inventory3, "3");
		StepVerifier.create(result)
					.expectNext(inventory3)
					.verifyComplete();
		
		verify(repository, times(1)).findById(Mockito.anyString());
		verify(repository, times(1)).save(Mockito.any());
		verify(client, times(1)).updateInventory(Mockito.any());
	}
 
	@Test
	void UpdateProductTest_NotFound() {
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<ProdInventoryDTO> result = services.updateProduct(inventory1, "1");
		StepVerifier.create(result)
					.expectNext()
					.verifyComplete();
		
		verify(repository, times(1)).findById(Mockito.anyString());
	}

	@Test
	void deleteProductTest_OnSuccess() {
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.just(product1));
		when(repository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
		when(client.deleteInventory(Mockito.any())).thenReturn(Mono.just(productInventoryDTO1));
		Mono<ProdInventoryDTO> product = services.deleteProduct("1");
		StepVerifier.create(product)
					.expectNext()
					.verifyComplete();
		
		verify(repository, times(1)).findById(Mockito.anyString());
		verify(repository, times(1)).deleteById(Mockito.anyString());
		verify(client, times(1)).deleteInventory(Mockito.any());
	}
 
	@Test
	void deleteProductTest_NotFound() {
		when(repository.findById(Mockito.anyString())).thenReturn(Mono.empty());
		when(repository.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
		when(client.deleteInventory(Mockito.anyString())).thenReturn(Mono.empty());
		Mono<ProdInventoryDTO> result = services.deleteProduct("1");
		
		StepVerifier.create(result)
					.expectNext()
					.verifyComplete();
		
		verify(repository, times(1)).findById(Mockito.anyString());
		verify(repository, times(1)).deleteById(Mockito.anyString());
		verify(client, times(1)).deleteInventory(Mockito.anyString());
	}
	
}

