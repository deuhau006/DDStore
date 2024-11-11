package com.shop.ddstore.service.impl;

import com.shop.ddstore.converter.UserConverter;
import com.shop.ddstore.dto.PaginationResults;
import com.shop.ddstore.dto.UserDTO;
import com.shop.ddstore.dto.authentication.SignUpRequest;
import com.shop.ddstore.entities.UserEntity;
import com.shop.ddstore.repositories.UserRepository;
import com.shop.ddstore.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {

	private final UserRepository userRepository;

	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				return userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not founded!"));
			}
		};
	}

	@Override
	public ResponseEntity<?> getUsers(int pageNo, int pageSize) {
		try {
			long totalItems = userRepository.count();
			int totalPages = (int) Math.ceil((double) totalItems / pageSize);
			
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
			Page<UserEntity> listUsers = userRepository.findAll(pageable);
			List<UserDTO> listUsersDTO = new ArrayList<>();
			for (UserEntity entity : listUsers) {
				UserDTO dto = UserConverter.toDTO(entity);
				listUsersDTO.add(dto);
			}
			
			PaginationResults<UserDTO> listResults = new PaginationResults<>();
			listResults.setData(listUsersDTO);
			listResults.setTotalItems(totalItems);
			listResults.setTotalPages(totalPages);
			
			return ResponseEntity.ok(listResults);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
		}
	}

	@Override
	public UserDTO findByEmail(String email) {
		UserEntity user = userRepository.findByEmail(email).get();
		UserDTO userDTO = UserConverter.toDTO(user);
		return userDTO;
	}

	@Override
	public ResponseEntity<?> updateUser(String email, SignUpRequest request) {
		try {
			System.out.println(request);
			UserEntity user = userRepository.findByEmail(email).get();
			user.setName(request.getName());
			user.setEmail(request.getEmail());
			user.setPhone(request.getPhone());
			user.setAddress(request.getAddress());
			
			System.out.println(user);
			
			userRepository.save(user);
			return ResponseEntity.ok("Cập nhật thông tin thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cập nhật thông tin không thành công.");
		}
	}
}
