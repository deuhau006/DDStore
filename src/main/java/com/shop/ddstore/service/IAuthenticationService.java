package com.shop.ddstore.service;

import com.shop.ddstore.dto.authentication.AuthenticationRequest;
import com.shop.ddstore.dto.authentication.JwtAuthenticationResponse;
import com.shop.ddstore.dto.authentication.RefreshTokenRequest;
import com.shop.ddstore.dto.authentication.SignUpRequest;
import org.springframework.http.ResponseEntity;


public interface IAuthenticationService {

	ResponseEntity<?> signup(SignUpRequest request);
	ResponseEntity<?> signin(AuthenticationRequest request);
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest request);
}
