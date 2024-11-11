package com.shop.ddstore.controllers;

import com.shop.ddstore.dto.authentication.AuthenticationRequest;
import com.shop.ddstore.dto.authentication.JwtAuthenticationResponse;
import com.shop.ddstore.dto.authentication.RefreshTokenRequest;
import com.shop.ddstore.dto.authentication.SignUpRequest;
import com.shop.ddstore.service.impl.AuthenticationServiceImpl;
import com.shop.ddstore.service.impl.JwtServiceImpl;
import com.shop.ddstore.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

	private final AuthenticationServiceImpl authenticationService;
	private final UserServiceImpl userService;
	private final JwtServiceImpl jwtService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest request) throws RuntimeException {
		return ResponseEntity.ok(authenticationService.signup(request));
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody AuthenticationRequest request) throws RuntimeException {
		ResponseEntity<?> response = authenticationService.signin(request);
		if (response == null) {
			// Authentication failed, return a 401 Unauthorized response
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		} else {
			// Authentication successful, return a 200 OK response
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest request) {
		return ResponseEntity.ok(authenticationService.refreshToken(request));
	}

	@GetMapping("/find")
	public ResponseEntity<?> findUser(@RequestHeader("Authorization") String jwtToken) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return ResponseEntity.ok(userService.findByEmail(userEmail));
	}

	@PutMapping("/update-info")
	public ResponseEntity<?> updateInfo(@RequestHeader("Authorization") String jwtToken,@RequestBody SignUpRequest request) {
		String token = jwtToken.substring(7);
		String userEmail = jwtService.extractUsername(token);
		return ResponseEntity.ok(userService.updateUser(userEmail, request));
	}
}
