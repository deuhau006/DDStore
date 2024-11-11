package com.shop.ddstore.converter;

import com.shop.ddstore.dto.CartItemDTO;
import com.shop.ddstore.entities.CartItemEntity;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter {
	
	public static CartItemDTO toDTO(CartItemEntity entity) {
		CartItemDTO dto = new CartItemDTO();
		dto.setId(entity.getId());
		dto.setQuantity(entity.getQuantity());
		dto.setTotalPrice(entity.getTotalPrice());
		dto.setProduct(ProductConverter.toDTO(entity.getProduct()));
		
		return dto;
	}
}
