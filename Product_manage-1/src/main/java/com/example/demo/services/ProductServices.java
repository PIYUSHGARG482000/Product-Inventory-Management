package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.configurations.ExternalClient;
import com.example.demo.models.ProdInventoryDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductInventoryDTO;
import com.example.demo.repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductServices {

		
	@Autowired
	private ProductRepo product_repo;
	@Autowired
	private ExternalClient client;
	ProductInventoryDTO dto = new ProductInventoryDTO();
		
	public ProductServices(ProductRepo product_repository, ExternalClient client) {
		this.product_repo = product_repository;
		this.client = client;
	}
	
	/**
	 * Function to fetch all product
	 * @return all the products
	 */
	public Flux<ProdInventoryDTO> getAllProduct() {
//		log.info("Fetching all the Products from Products Collection");		
//		Flux<Product> product = product_repo.findAll();  
//		Flux<ProductInventoryDTO> inventory = client.getAllInventory(); 
//		 
//		Flux<ProdInventoryDTO> fluxOfProductWithInventory = product.flatMap(prod ->  
//			inventory  
//		        .filter(invtry -> invtry.getProductId().equals(prod.getId()))
//		        .next() 
//		        .map(prodinventory -> new ProdInventoryDTO(prod, prodinventory.getStockCount(), prodinventory.getStatus())));	
		
		Flux<ProdInventoryDTO> fluxprod = Flux.zip(product_repo.findAll(), client.getAllInventory()).flatMap(tuple -> {
			Product product = tuple.getT1();
			ProductInventoryDTO inventory = tuple.getT2();
			
			if(inventory.getProductId().equals(product.getId())) {
				return Flux.just(ProdInventoryDTO.builder()
												  .product(product)
												  .StockCount(inventory.getStockCount())
												  .StockStatus(inventory.getStatus())
												  .build());
			}else {
				return Flux.empty();
			}
			
		});
		
		return fluxprod;
	}

	/**
	 * Function to fetch product by productId
	 * 
	 * @param id - ProductId to fetch product
	 * @return - product associated with productId
	 */
	@Cacheable(value="products")
	public Mono<ProdInventoryDTO> getProductById(String id) {
		log.info("Fetching Product associated with Id : " + id);

//		Mono<Product> product = product_repo.findById(id);
//		Mono<ProductInventoryDTO> inventory = client.getInventoryByProductId(id);
//		Mono.zip(product, inventory).flatMap(null)

		Mono<ProdInventoryDTO> prodDTO = Mono.zip(product_repo.findById(id), client.getInventoryByProductId(id)).flatMap(tuple -> {
						Product prod = tuple.getT1();
						ProductInventoryDTO inventory = tuple.getT2();
						return Mono.just(ProdInventoryDTO.builder()
														  .product(prod)
														  .StockCount(inventory.getStockCount())
														  .StockStatus(inventory.getStatus())
														  .build());
		});

//		Mono<ProdInventoryDTO> productById = product.flatMap(prod -> inventory.map(
//				prodinventory -> new ProdInventoryDTO(prod, prodinventory.getStockCount(), prodinventory.getStatus())));
		return prodDTO.switchIfEmpty(Mono.empty());
	}
	
	/**
	 * Function to add Product 
	 * @param product - product to add
	 * @return added product 
	 */
	public Mono<Product> addProduct(ProdInventoryDTO product) {
		log.info("Adding the Product with Name:"+product.getProduct().getName() + " in a Products Collection");
		
		Product Objproduct = Product.builder()
							   .name(product.getProduct().getName())
							   .description(product.getProduct().getDescription())
							   .price(product.getProduct().getPrice())
							   .build();
					
		return product_repo.save(Objproduct).flatMap(addproduct -> {
			log.info("Adding Product to Inventory throught WebClient");
			ProductInventoryDTO dto;
			if(product.getStockCount() > 0) {
				dto = ProductInventoryDTO.builder()
						  .productId(addproduct.getId())
						  .stockCount(product.getStockCount())
						  .status("IN_STOCK")
						  .build();
			}
			else if(product.getStockCount() == 0) {
				dto = ProductInventoryDTO.builder()
						  .productId(addproduct.getId())
						  .stockCount(product.getStockCount())
						  .status("OUT_OF_STOCK")
						  .build();
			}
			else {
				dto = ProductInventoryDTO.builder()
						  .productId(addproduct.getId())
						  .stockCount(product.getStockCount())
						  .status("AVAILABLE_SOON")
						  .build();
			}			
				return client.addInventory(dto).flatMap(inventorydto -> {
					return Mono.just(addproduct);
				});
		}).switchIfEmpty(Mono.empty());	
	}
	
	/**
	 * Function to update the Product data associated with the ProductId.
	 * 
	 * @param product - Updated Product Data
	 * @param id - Product Id 
	 * @return Updated product associated with the ProductId
	 */
	
	@CachePut(value="products", key="#a1")
	public Mono<ProdInventoryDTO> updateProduct(ProdInventoryDTO product, String id) {
		log.info("Updated the Product Details associated with Id : " + id + " in a Product collection");	
		
		return product_repo.findById(id).flatMap(existing -> {	
//			existing.setName(product.getProduct().getName());
//			existing.setDescription(product.getProduct().getDescription());
//			existing.setPrice(product.getProduct().getPrice());
			
			Product existingObj = Product.builder()
								  .id(existing.getId())
								  .name(product.getProduct().getName())
								  .description(product.getProduct().getDescription())
								  .price(product.getProduct().getPrice())
								  .build();
			
			return product_repo.save(existingObj);
		}).flatMap(updated -> {
			if(product.getStockCount() > 0) {
				dto = ProductInventoryDTO.builder()
						  .productId(updated.getId())
						  .stockCount(product.getStockCount())
						  .status("IN_STOCK")
						  .build();
				//dto.setStatus("IN_STOCK");
			}else if(product.getStockCount() == 0) {
				dto = ProductInventoryDTO.builder()
						  .productId(updated.getId())
						  .stockCount(product.getStockCount())
						  .status("OUT_OF_STOCK")
						  .build();
				//dto.setStatus("OUT_OF_STOCK");
			}else {
				dto = ProductInventoryDTO.builder()
						  .productId(updated.getId())
						  .stockCount(product.getStockCount())
						  .status("AVAILABLE_SOON")
						  .build();
				//dto.setStatus("AVAILABLE_SOON");
			}
//			System.out.println(updated.getId());
//			dto.setProductId(updated.getId());
//			dto.setStockCount(product.getStockCount());	
			log.info("Updating Product into Inventory throught WebClient");
			ProdInventoryDTO dtoprodDto = ProdInventoryDTO.builder()
										.product(updated)
										.StockCount(dto.getStockCount())
										.StockStatus(dto.getStatus())
										.build();
			return client.updateInventory(dto).flatMap(inventory -> {
				return Mono.just(dtoprodDto);				
			});
		}).switchIfEmpty(Mono.empty());
	}
	
	/**
	 * Function to delete product associated with productId.
	 * @param id - ProductId 
	 * @return - COnfirmation message for the product to be deleted associated with ProductId
	 */
	@CacheEvict(value="product")
	public Mono<ProdInventoryDTO> deleteProduct(String id) {
		log.warn("Product associated with Id:" + id + " removed from Products collection");
//		Mono<Product> product = product_repo.findById(id);  
//		Mono<ProductInventoryDTO> inventory =  client.deleteInventory(id);
//		
//		Mono<ProdInventoryDTO> productById =  product.flatMap(prod -> 
//			inventory.map(prodinventory -> new ProdInventoryDTO(prod, prodinventory.getStockCount(), prodinventory.getStatus()))
//		);					
//	 return product_repo.findById(id).zip() 
		
		Mono<ProdInventoryDTO> prodDTO = Mono.zip(product_repo.findById(id), client.deleteInventory(id),product_repo.deleteById(id)).flatMap(tuple -> {
			Product prod = tuple.getT1();
			ProductInventoryDTO inventory = tuple.getT2();
			//tuple.getT3();
			System.out.println("HERE IS THE INVENTORY : " + inventory);
			if(prod != null) {
				return Mono.just(ProdInventoryDTO.builder()
												 .product(prod)
												 .StockCount(inventory.getStockCount())
												 .StockStatus(inventory.getStatus())
												 .build());
			}else {
				return Mono.empty();
			}
		});		
		return prodDTO.switchIfEmpty(Mono.empty());
	}
}