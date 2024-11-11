package com.shop.ddstore.dto.authentication;

import lombok.Data;

@Data
public class SignUpRequest {

	private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
}
