package com.shop.ddstore.controllers;

import com.shop.ddstore.dto.request.CommentRequest;
import com.shop.ddstore.service.impl.CommentServiceImpl;
import com.shop.ddstore.service.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private CommentServiceImpl commentService;

	@PostMapping("/post-comment/{id}")
	public ResponseEntity<?> postComment(@RequestHeader("Authorization") String jwtToken,
			@RequestBody CommentRequest request, @PathVariable("id") Long productId) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return commentService.saveComment(request, userEmail, productId);
	}
	
	@GetMapping("/get-all-comments-for-product/{id}")
	public ResponseEntity<?> getAllCommentsForProduct(@PathVariable("id") Long productId) {
		return commentService.getAllCommentsForProduct(productId);
	}
}
