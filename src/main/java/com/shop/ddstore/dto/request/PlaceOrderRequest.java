package com.shop.ddstore.dto.request;

import lombok.Data;

@Data
public class PlaceOrderRequest {

	String username;
	String phone;
	String address;
	String payment;
}
