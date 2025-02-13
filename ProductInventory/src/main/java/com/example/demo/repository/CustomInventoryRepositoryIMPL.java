package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.demo.models.ProductInventory;

import reactor.core.publisher.Mono;

public class CustomInventoryRepositoryIMPL implements CustomInventoryRepository {

	@Autowired
	private ReactiveMongoTemplate template;
	
	
	@Override
	public Mono<ProductInventory> findByProductId(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where(productId).is(productId));
		return template.findById(query, ProductInventory.class);
	}

	@Override
	public Mono<ProductInventory> deleteByProductId(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where(productId).is(productId));
		return template.findAndRemove(query, ProductInventory.class);
	}
	
}
