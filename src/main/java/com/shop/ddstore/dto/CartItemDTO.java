package com.shop.ddstore.dto;

import lombok.Data;

@Data
public class CartItemDTO {

	private Long id;
	
	private ShoppingCartDTO cart;
	
	private ProductDTO product;
	
	private int quantity;
	
	private double totalPrice;
}
