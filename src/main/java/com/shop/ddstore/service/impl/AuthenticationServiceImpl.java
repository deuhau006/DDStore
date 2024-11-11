package com.shop.ddstore.service.impl;

import com.shop.ddstore.dto.authentication.AuthenticationRequest;
import com.shop.ddstore.dto.authentication.JwtAuthenticationResponse;
import com.shop.ddstore.dto.authentication.RefreshTokenRequest;
import com.shop.ddstore.dto.authentication.SignUpRequest;
import com.shop.ddstore.entities.Role;
import com.shop.ddstore.entities.UserEntity;
import com.shop.ddstore.repositories.UserRepository;
import com.shop.ddstore.response.ErrorResponse;
import com.shop.ddstore.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JwtServiceImpl jwtServiceImpl;

	public ResponseEntity<?> signup(SignUpRequest request) {
		try {
			var userEmailOptional = userRepository.findByEmail(request.getEmail());
			var userPhoneOptional = userRepository.findByPhone(request.getPhone());

			if (userEmailOptional.isPresent() && userPhoneOptional.isPresent()) {
				ErrorResponse errorResponse = new ErrorResponse("Email và số điện thoại đã được sử dụng.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
			} else if (userEmailOptional.isPresent()) {
				ErrorResponse errorResponse = new ErrorResponse("Email đã được sử dụng.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
			} else if (userPhoneOptional.isPresent()) {
				ErrorResponse errorResponse = new ErrorResponse("Số điện thoại đã được sử dụng.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
			} else {
				UserEntity newUser = new UserEntity();

				newUser.setEmail(request.getEmail());
				newUser.setName(request.getName());
				newUser.setPhone(request.getPhone());
				newUser.setPassword(passwordEncoder.encode(request.getPassword()));
				newUser.setAddress(request.getAddress());
				newUser.setRole(Role.USER);
				userRepository.save(newUser);

				return ResponseEntity.ok("Đăng kí tài khoản thành công.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đăng kí tài khoản không thành công.");
		}
	}

	// JwtAuthenticationResponse
	public ResponseEntity<?> signin(AuthenticationRequest request) {
		try {
	        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

	        var user = userRepository.findByEmail(request.getEmail()).get();
	        if (user == null) {
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không chính xác.");
	        }

	        var jwt = jwtServiceImpl.generateToken(user);
	        var refreshToken = jwtServiceImpl.generateRefreshToken(new HashMap<>(), user);

	        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
	        jwtAuthenticationResponse.setToken(jwt);
	        jwtAuthenticationResponse.setRefreshToken(refreshToken);
	        jwtAuthenticationResponse.setName(user.getName());
	        jwtAuthenticationResponse.setEmail(user.getEmail());
	        jwtAuthenticationResponse.setRole(user.getRole().name());

	        return ResponseEntity.ok(jwtAuthenticationResponse);
	    } catch (AuthenticationException e) {
	        ErrorResponse errorResponse = new ErrorResponse("Email hoặc mật khẩu không chính xác.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	    }
	}

	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
		String userEmail = jwtServiceImpl.extractUsername(request.getToken());
		UserEntity user = userRepository.findByEmail(userEmail).orElseThrow();
		if (jwtServiceImpl.isTokenValid(request.getToken(), user)) {
			var jwt = jwtServiceImpl.generateToken(user);

			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(request.getToken());

			return jwtAuthenticationResponse;
		}
		return null;
	}
	
	@PostConstruct
	public void createAdminAccount() {
		UserEntity admin = userRepository.findByRole(Role.ADMIN);
		if (admin == null) {
			UserEntity user = new UserEntity();
			user.setName("admin");
			user.setEmail("admin@gmail.com");
			user.setPassword(passwordEncoder.encode("12345678"));
			user.setName("admin");
			user.setRole(Role.ADMIN);
			userRepository.save(user);
		}
	}
}
