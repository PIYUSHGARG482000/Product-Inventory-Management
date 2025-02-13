package com.example.demo.repository;

import com.example.demo.models.ProductInventory;

import reactor.core.publisher.Mono;

public interface CustomInventoryRepository {
	Mono<ProductInventory> findByProductId(String productId);
	Mono<ProductInventory> deleteByProductId(String productId);
}
