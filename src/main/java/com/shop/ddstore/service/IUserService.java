package com.shop.ddstore.service;

import com.shop.ddstore.dto.UserDTO;
import com.shop.ddstore.dto.authentication.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService {

	ResponseEntity<?> getUsers(int pageNo, int pageSize);
	UserDetailsService userDetailsService();
	UserDTO findByEmail(String email);
	ResponseEntity<?> updateUser(String email, SignUpRequest request);
}
