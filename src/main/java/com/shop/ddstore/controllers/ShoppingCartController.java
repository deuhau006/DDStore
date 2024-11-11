package com.shop.ddstore.controllers;

import com.shop.ddstore.service.impl.JwtServiceImpl;
import com.shop.ddstore.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartServiceImpl shoppingCartService;

	@Autowired
	private JwtServiceImpl jwtService;

	@PostMapping("/add-product")
	public ResponseEntity<?> addProductToCart(
			@RequestParam String email,
			@RequestParam Long productId,
			@RequestParam int quantity) {
		return shoppingCartService.addProductToCart(email, productId, quantity);
	}

	@GetMapping("/get-cart-item")
	public ResponseEntity<?> getCartItemsForUser(@RequestHeader("Authorization") String jwtToken) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return shoppingCartService.getCartItemsForUser(userEmail);
	}
	
	@GetMapping("/get-shopping-cart")
	public ResponseEntity<?> getShoppingCartByUser(@RequestHeader("Authorization") String jwtToken) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return ResponseEntity.ok(shoppingCartService.findShoppingCartByUser(userEmail));
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<?> removeProductFromCart(@RequestHeader("Authorization") String jwtToken,
			@PathVariable("id") Long productId) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return shoppingCartService.removeItemFromShoppingCart(userEmail, productId);
	}
}
