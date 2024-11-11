package com.shop.ddstore.converter;

import com.shop.ddstore.dto.OrderDTO;
import com.shop.ddstore.dto.OrderItemDTO;
import com.shop.ddstore.entities.OrderEntity;
import com.shop.ddstore.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrderConverter {

	public static OrderDTO toDTO(OrderEntity entity) {
		OrderDTO dto = new OrderDTO();
		dto.setId(entity.getId());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setUsername(entity.getUsername());
		dto.setBillingAddress(entity.getBillingAddress());
		dto.setOrderAmt(entity.getOrderAmt());
		dto.setOrderStatus(entity.getOrderStatus().getDisplayValue());
		dto.setTotalPricesOrder(entity.getTotalPricesOrder());
		dto.setPaymentStatus(entity.getPaymentStatus());
		dto.setUser(UserConverter.toDTO(entity.getUser()));
		dto.setPhone(entity.getPhone());
		
		Set<OrderItemEntity> orderItemsEntity = entity.getOrderItems();
		Set<OrderItemDTO> orderItemsDTO = new HashSet<>();
		for (OrderItemEntity itemEntity : orderItemsEntity) {
			OrderItemDTO itemDTO = OrderItemConverter.toDTO(itemEntity);
			orderItemsDTO.add(itemDTO);
		}
		
		dto.setOrderItem(orderItemsDTO);
		
		return dto;
	}
}
