package com.example.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ProductInventory;

@Repository
public interface InventoryRepository  extends ReactiveMongoRepository<ProductInventory, String>, CustomInventoryRepository{

}
