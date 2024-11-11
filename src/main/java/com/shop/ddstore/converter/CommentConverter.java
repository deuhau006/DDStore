package com.shop.ddstore.converter;

import com.shop.ddstore.dto.CommentDTO;
import com.shop.ddstore.entities.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

	public static CommentDTO toDTO(CommentEntity entity) {
		CommentDTO dto = new CommentDTO();
		dto.setId(entity.getId());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setContent(entity.getContent());
		dto.setRating(entity.getRating());
		dto.setUsername(entity.getUser().getName());
		
		return dto;
	}
}
