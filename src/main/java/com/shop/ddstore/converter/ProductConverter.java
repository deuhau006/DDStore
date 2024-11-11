package com.shop.ddstore.converter;

import com.shop.ddstore.dto.ProductDTO;
import com.shop.ddstore.entities.ProductEntity;
import org.springframework.stereotype.Component;


@Component
public class ProductConverter {

	public static ProductEntity toEntity(ProductDTO dto) {
		ProductEntity entity = new ProductEntity();
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setName(dto.getName());
		entity.setType(dto.getType());
		entity.setDescription(dto.getDescription());
		entity.setOldPrice(dto.getOldPrice());
		entity.setSale(dto.getSale());
		entity.setNewPrice(dto.getNewPrice());
		entity.setRating(dto.getRating());
		entity.setImageUrl(dto.getImageUrl());
		entity.setImageId(dto.getImageId());
		entity.setValid(dto.getValid());
		
		return entity;
	}
	
	public static ProductDTO toDTO(ProductEntity entity) {
		ProductDTO dto = new ProductDTO();
		if (entity.getId() != null) {
			dto.setId(entity.getId());
		}
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setType(entity.getType());
		dto.setOldPrice(entity.getOldPrice());
		dto.setSale(entity.getSale());
		dto.setNewPrice(entity.getNewPrice());
		dto.setRating(entity.getRating());
		dto.setImageUrl(entity.getImageUrl());
		dto.setImageId(entity.getImageId());
		dto.setValid(entity.getValid());
		
		return dto;
	}
	
	public static ProductEntity toEntity(ProductDTO dto, ProductEntity entity) {
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setType(dto.getType());
		entity.setOldPrice(dto.getOldPrice());
		entity.setSale(dto.getSale());
		entity.setNewPrice(dto.getNewPrice());
		entity.setRating(dto.getRating());
		entity.setImageUrl(dto.getImageUrl());
		entity.setImageId(dto.getImageId());
		entity.setValid(dto.getValid());
		
		return entity;
	}
}
