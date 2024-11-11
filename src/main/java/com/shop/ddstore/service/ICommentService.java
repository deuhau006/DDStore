package com.shop.ddstore.service;

import com.shop.ddstore.dto.request.CommentRequest;
import org.springframework.http.ResponseEntity;

public interface ICommentService {

	ResponseEntity<?> saveComment(CommentRequest request, String email, Long productId);
	ResponseEntity<?> getAllCommentsForProduct(Long productId);
	Double getAverageRatingForProduct(Long productId);
}
