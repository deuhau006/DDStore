package com.shop.ddstore.converter;

import com.shop.ddstore.dto.UserDTO;
import com.shop.ddstore.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

	public static UserEntity toEntity(UserDTO dto) {
		UserEntity entity = new UserEntity();
		entity.setId(dto.getId());
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		entity.setAddress(dto.getAddress());
		entity.setRole(dto.getRole());
		
		return entity;
	}
	
	public static UserDTO toDTO(UserEntity entity) {
		UserDTO dto = new UserDTO();
		dto.setId(entity.getId());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setName(entity.getName());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		dto.setPassword(entity.getPassword());
		dto.setAddress(entity.getAddress());
		dto.setRole(entity.getRole());
		
		return dto;
	}
	
	public static UserEntity toEntity(UserDTO dto, UserEntity entity) {
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setName(dto.getName());
		entity.setPhone(dto.getPhone());
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		entity.setAddress(dto.getAddress());
		entity.setRole(dto.getRole());
		
		return entity;
	}
}
