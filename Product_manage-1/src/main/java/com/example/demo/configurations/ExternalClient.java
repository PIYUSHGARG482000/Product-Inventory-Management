package com.example.demo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.models.ProductInventoryDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class ExternalClient {
	
	@Autowired
	private WebClient webclient;
	
	public ExternalClient(WebClient webclient) {
		this.webclient = webclient;
	}
	
	
	public Flux<ProductInventoryDTO> getAllInventory(){
		return webclient.get()
						 .retrieve()
						 .bodyToFlux(ProductInventoryDTO.class);
	}
	
	public Mono<ProductInventoryDTO> getInventoryByProductId(String productId){
		return webclient.get()
						.uri("/product/{id}", productId)
						.retrieve()
						.bodyToMono(ProductInventoryDTO.class);
	}
		
	/**
	 * Function for external webclient call to add Inventory 
	 * @param dto - Inventory to add
	 * @return - added Inventory
	 */
	public Mono<ProductInventoryDTO> addInventory(ProductInventoryDTO dto){
		return webclient.post()   //Webclient call block is used to call synchronously backend api;
						.uri("/addInventory")
						.bodyValue(dto)
						.retrieve()
						.bodyToMono(ProductInventoryDTO.class);		
		
	}
	
	/**
	 * Function for external Webclient call to Update Inventory
	 * @param dto - updated Inventory Object
	 * @return - Updated Inventory Object
	 */	
	public Mono<ProductInventoryDTO> updateInventory(ProductInventoryDTO dto){
		return webclient.put()
				.uri("/product/update/{id}",dto.getProductId())
				.bodyValue(dto)
				.retrieve()
				.bodyToMono(ProductInventoryDTO.class);
	}	
	
	/**
	 * Function for external webclient call to delete Inventory
	 * @param productId
	 * @return - Inventory Deleted associated with ProductId
	 */
	public Mono<ProductInventoryDTO> deleteInventory(String id){
		return webclient.delete()
						.uri("/product/delete/{id}", id)
						.retrieve()
						.bodyToMono(ProductInventoryDTO.class);
	}
	
}