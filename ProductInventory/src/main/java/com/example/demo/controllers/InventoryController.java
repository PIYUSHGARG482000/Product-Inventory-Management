package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ProductInventory;
import com.example.demo.models.Status;
import com.example.demo.services.InventoryServices;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryServices inventoryservices;
	
	
	public InventoryController(InventoryServices inventoryservice) {
		this.inventoryservices = inventoryservice;
	}	
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public Flux<ProductInventory> getAllInventory(){
		return inventoryservices.getAllInventory();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> getInventoryById(@PathVariable("id") String StockId){
		return inventoryservices.getInventoryByStockId(StockId);
	}
 	
	@GetMapping("/product/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> getInventoryByProductId(@PathVariable("id") String productId){
		return inventoryservices.getInventoryByProductId(productId);
	}
	
	
	
	@PostMapping("/addInventory")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ProductInventory> addInventory(@RequestBody ProductInventory inventory){
		return inventoryservices.addToInventory(inventory);
	}
	
	@PutMapping("/update/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<ProductInventory> updateInventory(@RequestBody ProductInventory inventory, @PathVariable("id") String StockId){
		return inventoryservices.updateToInventory(inventory, StockId);
	}
	
	@PutMapping("/product/update/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<ProductInventory> updateInventoryByProductId(@RequestBody ProductInventory inventory, @PathVariable("id") String productId){
		return inventoryservices.updateByProductId(inventory, productId);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductInventory> deleteInventroy(@PathVariable("id") String StockId){
		return inventoryservices.deleteFromInventory(StockId);
	}
		
	@DeleteMapping("/product/delete/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<ProductInventory> deleteByProductId(@PathVariable("id") String productId){
		return inventoryservices.deleteByProductId(productId);
	}
	
//	@GetMapping("/products")
//	@ResponseStatus(HttpStatus.OK)
//	public Flux<Product> getAllProducts(){
//		return inventoryservices.getAllInventory();
//	}
	
	@GetMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public Flux<ProductInventory> getInventoryByStatus(@RequestParam(value="status", required=true) Status status){
		return inventoryservices.getInventoryByStatus(status);
	}
}
