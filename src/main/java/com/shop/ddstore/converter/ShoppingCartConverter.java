package com.shop.ddstore.converter;

import com.shop.ddstore.dto.CartItemDTO;
import com.shop.ddstore.dto.ShoppingCartDTO;
import com.shop.ddstore.entities.CartItemEntity;
import com.shop.ddstore.entities.ShoppingCartEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ShoppingCartConverter {

	public static ShoppingCartDTO toDTO(ShoppingCartEntity entity) {
		ShoppingCartDTO dto = new ShoppingCartDTO();
		dto.setId(entity.getId());
		dto.setTotalItems(entity.getTotalItems());
		dto.setTotalPrices(entity.getTotalPrice());
		
		Set<CartItemDTO> cartItemsDTO = new HashSet<>();
		for(CartItemEntity cartItem : entity.getCartItems()) {
			CartItemDTO cartDTO = CartItemConverter.toDTO(cartItem);
			cartItemsDTO.add(cartDTO);
		}
		dto.setCartItemsDTO(cartItemsDTO);
		
		return dto;
	}
}
