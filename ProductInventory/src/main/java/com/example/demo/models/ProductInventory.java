package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Inventory")
public class ProductInventory {
	
	@Id
	private String stockId;
	private int stockCount;
	private Status status;
	private String productId;

}
