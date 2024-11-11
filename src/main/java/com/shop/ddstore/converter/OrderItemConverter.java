package com.shop.ddstore.converter;

import com.shop.ddstore.dto.OrderItemDTO;
import com.shop.ddstore.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter {

	public static OrderItemDTO toDTO(OrderItemEntity entity) {
		OrderItemDTO dto = new OrderItemDTO();
		dto.setId(entity.getId());
		dto.setQuantity(entity.getQuantity());
		dto.setTotalPrice(entity.getTotalPrice());
		dto.setProduct(ProductConverter.toDTO(entity.getProduct()));
		
//		OrderDTO orderDTO = OrderConverter.toDTO(entity.getOrder());
//		dto.setOrder(orderDTO);
		
		return dto;
	}
}
