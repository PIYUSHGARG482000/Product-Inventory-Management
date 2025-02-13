package com.example.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Product;

@Repository
public interface ProductRepo extends ReactiveMongoRepository<Product, String> {

}
