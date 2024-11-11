package com.shop.ddstore.repositories;

import com.shop.ddstore.entities.Role;
import com.shop.ddstore.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByPhone(String phone);
	UserEntity findByRole(Role role);
}
