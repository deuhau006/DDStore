package com.shop.ddstore.service;

import com.shop.ddstore.dto.OrderStatusSummary;
import com.shop.ddstore.dto.request.PlaceOrderRequest;
import com.shop.ddstore.entities.OrderEntity;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface IOrderService {

	OrderEntity orderCreate(PlaceOrderRequest request, String email);
	ResponseEntity<?> findOrderByUUID(String trackingUUID);
	ResponseEntity<?> getAllOrderFromUser(String email);
	ResponseEntity<?> getAllOrders(int pageNo, int pageSize);
	ResponseEntity<?> updateOrderStatus(Long orderId, int status);
	ResponseEntity<?> getTotalPriceInDateRange(Date startDate, Date endDate);
	ResponseEntity<?> getTotalPriceByMonthInYear(int year);
	ResponseEntity<?> getOneOrder(String email, Long orderId);
	ResponseEntity<?> findOrderById(Long orderId);
	OrderStatusSummary countOrdersByStatus();
}
