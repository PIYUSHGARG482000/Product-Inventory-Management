package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ProdInventoryDTO;
import com.example.demo.models.Product;
import com.example.demo.services.ProductServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@Slf4j
@CacheConfig(cacheNames="products")
@Tag(name = "Product Management", description = "Product and Inventory Management API\'s")
public class ProductController {

	@Autowired
	private ProductServices productservice;
	
	public ProductController(ProductServices productservice) {
		this.productservice = productservice;
	}
			
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Retreive all Products.", description = "Fetch all products associated with their inventory data.")
	public Flux<ProdInventoryDTO> getAllProduct(){
		log.info("All products feteched from database.");
		return productservice.getAllProduct();
	}
		
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Retreive Product associated with Id.", description = "Fetch product Object associated with the Id provided.")
	@Cacheable(value = "products",key="#id", condition="#id!=null")
	public Mono<ProdInventoryDTO> getProductById(@PathVariable("id") String id){
		log.info("Product associated with id : " + id + " fetched from the database.");
		return productservice.getProductById(id);
	}

	@PostMapping("/addproduct")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create new Product.", description = "Creats new Product entry with Inventory details.")
	public Mono<Product> addProduct(@RequestBody ProdInventoryDTO product){
		log.info("Product : " + product.getProduct().toString() + " added in the database.");
		return productservice.addProduct(product);
	}
	
	@PutMapping("/update/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Updates Exisiting Product associated with Id.", description = "Modify existing product Object associated with the Id provided.")
	@CachePut(value="products", key="#a0") 
	public Mono<ProdInventoryDTO> updateProduct(@PathVariable("id") String id, @RequestBody ProdInventoryDTO product){
		log.info("Product associated with id : " + id + " updated in database.");
		return productservice.updateProduct(product, id);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Delete Exisiting Product associated with Id.", description = "Removes Existing product Object associated with the Id provided.")
	@CacheEvict(value = "product",key="#id", condition="#id!=null")
	public Mono<ProdInventoryDTO> deleteProduct(@PathVariable("id") String id){
		log.info("Product associated with id : " + id + " removed from the database.");
		return productservice.deleteProduct(id);
	}	
}