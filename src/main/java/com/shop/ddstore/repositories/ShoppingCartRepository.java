package com.shop.ddstore.repositories;

import com.shop.ddstore.entities.ShoppingCartEntity;
import com.shop.ddstore.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {
	
	ShoppingCartEntity findByUser(UserEntity user);
}
