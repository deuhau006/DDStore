package com.shop.ddstore.controllers;

import com.shop.ddstore.dto.request.PlaceOrderRequest;
import com.shop.ddstore.service.impl.JwtServiceImpl;
import com.shop.ddstore.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderServiceImpl orderService;

	@Autowired
	private JwtServiceImpl jwtService;

	@PostMapping("/create")
	public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") String jwtToken,
			@RequestBody PlaceOrderRequest request) {
		try {
			String token = jwtToken.substring(7);
			String userEmail = jwtService.extractUsername(token);
			orderService.orderCreate(request, userEmail);
			return ResponseEntity.ok("Đặt hàng thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đặt hàng không thành công.");
		}
	}

	@GetMapping("/getAllOrder")
	public ResponseEntity<?> findAllOrdersFromUser(@RequestHeader("Authorization") String jwtToken) {
		try {
			String token = jwtToken.substring(7);
			String email = jwtService.extractUsername(token);
			return orderService.getAllOrderFromUser(email);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found!");
		}
	}

	@GetMapping("/findByUUID/{uuid}")
	public ResponseEntity<?> findOrderByUUID(@PathVariable("uuid") String uuid) {
		return orderService.findOrderByUUID(uuid);
	}

	@PutMapping("/updateOrderStatus/{id}/{status}")
	public ResponseEntity<?> updateOrderStatus(@RequestHeader("Authorization") String jwtToken,
			@PathVariable("id") Long orderId,@PathVariable("status") int status) {
		return orderService.updateOrderStatus(orderId, status);
	}
	
	@GetMapping("/getOneOrder/{id}")
	public ResponseEntity<?> getOneOrder(@RequestHeader("Authorization") String jwtToken,
			@PathVariable("id") Long orderId) {
		String token = jwtToken.substring(7);
		String email = jwtService.extractUsername(token);
		return orderService.getOneOrder(email, orderId);
	}
}
