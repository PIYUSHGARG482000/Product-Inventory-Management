package com.example.demo.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInventoryDTO implements Serializable{

	private String StockId;
	private int stockCount;
	private String status;
	private String productId;	
}
