package com.shop.ddstore.service.impl;

import com.shop.ddstore.converter.CommentConverter;
import com.shop.ddstore.dto.CommentDTO;
import com.shop.ddstore.dto.request.CommentRequest;
import com.shop.ddstore.entities.CommentEntity;
import com.shop.ddstore.entities.ProductEntity;
import com.shop.ddstore.entities.UserEntity;
import com.shop.ddstore.repositories.CommentRepository;
import com.shop.ddstore.repositories.ProductRepository;
import com.shop.ddstore.repositories.UserRepository;
import com.shop.ddstore.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private ProductRepository productRepository;

	@Override
	public ResponseEntity<?> saveComment(CommentRequest request, String email, Long productId) {
		try {
			UserEntity user = userRepository.findByEmail(email).get();
			ProductEntity product = productRepository.findById(productId).get();
			
			CommentEntity comment = new CommentEntity();
			comment.setContent(request.getContent());
			comment.setRating(request.getRating());
			comment.setUser(user);
			comment.setProduct(product);
			
			commentRepository.save(comment);
			
			double rating = commentRepository.getAverageRatingForProduct(productId);
			product.setRating(rating);
			productRepository.save(product);
			
			return ResponseEntity.ok("Đã đánh giá sản phẩm.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e);
		}
	}

	@Override
	public ResponseEntity<?> getAllCommentsForProduct(Long productId) {
		try {
			List<CommentEntity> listResults = commentRepository.findByProductId(productId);
			List<CommentDTO> listResultsDTO = new ArrayList<>();
			for (CommentEntity entity : listResults) {
				CommentDTO dto = CommentConverter.toDTO(entity);
				listResultsDTO.add(dto);
			}
			
			return ResponseEntity.ok(listResultsDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra: " + e);
		}
	}

	@Override
	public Double getAverageRatingForProduct(Long productId) {
		double rating = commentRepository.getAverageRatingForProduct(productId);
		ProductEntity product = productRepository.findById(productId).get();
		product.setRating(rating);
		
		return rating;
	}
}
