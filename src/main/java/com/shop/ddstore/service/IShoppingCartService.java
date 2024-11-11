package com.shop.ddstore.service;

import org.springframework.http.ResponseEntity;

public interface IShoppingCartService {

	ResponseEntity<?> addProductToCart(String email, Long productId, int quantity);
	ResponseEntity<?> getCartItemsForUser(String userEmail);
	ResponseEntity<?> findShoppingCartByUser(String email);
	ResponseEntity<?> removeItemFromShoppingCart(String email, Long productId);
}
