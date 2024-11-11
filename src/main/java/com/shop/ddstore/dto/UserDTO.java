package com.shop.ddstore.dto;

import com.shop.ddstore.entities.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

	private Long id;
	private Date createdDate;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Role role;
}
