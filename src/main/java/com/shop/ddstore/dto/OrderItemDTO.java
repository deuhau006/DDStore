package com.shop.ddstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

	private Long id;
	
	private ProductDTO product;
	
	private double totalPrice;
	
	private int quantity;
	
	@JsonIgnore
	private OrderDTO order;
}
