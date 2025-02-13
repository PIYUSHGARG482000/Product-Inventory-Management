package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.ProductInventory;
import com.example.demo.models.Status;
import com.example.demo.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class InventoryServices {
	
	@Autowired
	private InventoryRepository repository;
	
	public InventoryServices(InventoryRepository repo) {
		this.repository = repo;
	}
	
	/**
	 * Function to get all the inventory data for stock
	 * @return All the inventory Data of the stocks
	 */
	public Flux<ProductInventory> getAllInventory(){
		log.info("Fetchoing all the Inventory from collection");
		return repository.findAll();
	}
	
	
	/**
	 * Function to get inventory data for associated ProductId
	 * @param productId 
	 * @return the inventory data associated with productId
	 */
	public Mono<ProductInventory> getInventoryByProductId(String productId){
		log.info("Fetching Inventory Details associated with ProductId : " + productId);
		return repository.findByProductId(productId);
	}	
	
	/**
	 * Function to delete the Inventory by using productId
	 * @param productId
	 * @return the inventory associated with the productId
	 */
	public Mono<ProductInventory> deleteByProductId(String productId){	
		log.info("Deleting Inventory Details associated with ProductId : " + productId);
		return repository.deleteByProductId(productId);
	}
	
	/**
	 * Function to update the inventory details by fetching through ProductId
	 * @param updated - Updated inventory object
	 * @param productId - ProductId associated with the inventory
	 * @return the updated inventory associated with productId
	 */
	public Mono<ProductInventory> updateByProductId(ProductInventory updated, String productId){
		log.info("Updating Inventory Details associated with ProductId : " + productId);
		return repository.findByProductId(productId).flatMap(inventory -> {
			inventory.setStockCount(updated.getStockCount());
			if(inventory.getStockCount() > 0) 
				inventory.setStatus(Status.IN_STOCK);
			else if(inventory.getStockCount() == 0)
				inventory.setStatus(Status.OUT_OF_STOCK);
			else
				inventory.setStatus(Status.AVAILABLE_SOON);
			return repository.save(inventory).then(Mono.just(inventory));
//			return Mono.just(inventory);
		}).switchIfEmpty(Mono.empty());
	}	
	
	/**
	 * Function to get Inventory data by the StockId
	 * @param StockId
	 * @return the inventory data associated with the StockId
	 */
	public Mono<ProductInventory> getInventoryByStockId(String StockId){
		log.info("Fetching Inventory Details associated with StockId : " + StockId);
		return repository.findById(StockId).flatMap(inventory -> {
			return Mono.just(inventory);
		}).switchIfEmpty(Mono.empty());
	}
	
	/**
	 * Function to add the inventory data
	 * @param Inventory - Inventory data to add
	 * @return the Added Inventory 
	 */
	public Mono<ProductInventory> addToInventory(ProductInventory Inventory){
		log.info("Adding new Inventory Details in the collection with Inventory  : " + Inventory);
		if(Inventory.getStockCount() > 0) 
			Inventory.setStatus(Status.IN_STOCK);
		else if(Inventory.getStockCount() == 0)
			Inventory.setStatus(Status.OUT_OF_STOCK);
		else
			Inventory.setStatus(Status.AVAILABLE_SOON);
		
		return repository.save(Inventory).switchIfEmpty(Mono.empty());
	}
	
	/**
	 * Function to update the inventory by using StockId
	 * @param updated - Updated inventory data 
	 * @param StockId
	 * @return the updated Inventory data associated with the stockId
	 */
	public Mono<ProductInventory> updateToInventory(ProductInventory updated, String StockId){
		log.info("Updating Inventory Details associated with StockId : " + StockId);
		return repository.findById(StockId).flatMap(inventory -> {
			if(inventory.getStockCount() > 0) 
				inventory.setStatus(Status.IN_STOCK);
			else if(inventory.getStockCount() == 0)
				inventory.setStatus(Status.OUT_OF_STOCK);
			else
				inventory.setStatus(Status.AVAILABLE_SOON);
			
			inventory.setStockCount(updated.getStockCount());
			inventory.setProductId(updated.getProductId());
			return repository.save(inventory);
		}).switchIfEmpty(Mono.empty());
	}
	
	/**
	 * Function to delete the inventory data by StockId 
	 * @param StockId
	 * @return the deleted Inventory data associated with StockId
	 */
	public Mono<ProductInventory> deleteFromInventory(String StockId){
		log.info("Deleting Inventory Details associated with StockId : " + StockId);
		return repository.findById(StockId).flatMap(inventory -> {
			repository.deleteById(StockId);
			return Mono.just(inventory);
		}).switchIfEmpty(Mono.empty());
	}
	
	/**
	 * Function to return Inventory by Stock Status	
	 * @param status
	 * @return all the inventory associated with the status
	 */
	public Flux<ProductInventory> getInventoryByStatus(Status status){
		log.info("Fetching Inventory Details as per Stock Status : " + status);
		return repository.findAll().filter(inventory -> {
//			inventory.getStatus().toString().equalsIgnoreCase(s);
			return inventory.getStatus().toString().equalsIgnoreCase(status.toString());
		});
	}	
	
}