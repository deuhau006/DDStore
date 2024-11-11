package com.shop.ddstore.repositories;

import com.shop.ddstore.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	List<CommentEntity> findByProductId(Long productId);
	
	@Query("SELECT AVG(c.rating) FROM CommentEntity c WHERE c.product.id = :productId")
    Double getAverageRatingForProduct(@Param("productId") Long productId);
}
