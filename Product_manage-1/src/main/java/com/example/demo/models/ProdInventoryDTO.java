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
public class ProdInventoryDTO implements Serializable{
	private Product product;
	private int StockCount;
	private String StockStatus;
}
