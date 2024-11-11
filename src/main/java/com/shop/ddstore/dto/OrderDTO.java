package com.shop.ddstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	private Long id;

	private Date createdDate;
	
	private String username;

	private String paymentStatus;
	
	private String billingAddress;
	
	private String phone;

	private String orderStatus;

	private double orderAmt;
	
	private double totalPricesOrder;
	
	private UUID trackingUUID;

	private UserDTO user;

	private Set<OrderItemDTO> orderItem = new HashSet<>();
}
