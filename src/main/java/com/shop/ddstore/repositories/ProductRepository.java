package com.shop.ddstore.repositories;

import com.shop.ddstore.entities.ProductEntity;
import com.shop.ddstore.entities.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	@Query("SELECT p FROM ProductEntity p WHERE p.valid = :valid")
	Page<ProductEntity> findAllValidProducts(@Param("valid") ProductStatus valid, Pageable pageable);
	
	Page<ProductEntity> findByValidAndNewPriceLessThanEqual(ProductStatus valid, Pageable pageable, Long maxPrice);
	
	Optional<ProductEntity> getOne(long id);
	
	Optional<ProductEntity> findFirstByValidOrderByNewPriceDesc(@Param("valid") ProductStatus valid);
	
	Optional<ProductEntity> findByNameAndValid(@Param("name") String name, @Param("valid") ProductStatus valid);

	List<ProductEntity> findByNameContainingAndValid(@Param("name") String name, @Param("valid") ProductStatus valid);

	@Query("SELECT p FROM ProductEntity p WHERE p.type IN :types AND p.valid = :valid")
	Page<ProductEntity> findByTypeAndValid(@Param("types") List<String> types, @Param("valid") ProductStatus valid, Pageable pageable);
	
	@Query("SELECT p FROM ProductEntity p WHERE p.type IN :types AND p.newPrice >= :minPrice AND p.valid = :valid")
	Page<ProductEntity> findByTypeAndMinPrice(List<String> types, Long minPrice,@Param("valid") ProductStatus valid, Pageable pageable);

	List<ProductEntity> findTop5ByTypeAndValidOrderByTypeDesc(String type, ProductStatus valid);
	
	@Query("SELECT p FROM ProductEntity p WHERE p.newPrice BETWEEN :minPrice AND :maxPrice AND p.valid = :valid")
	Page<ProductEntity> findByPriceRange(@Param("minPrice") Long minPrice,
			@Param("maxPrice") Long maxPrice,@Param("valid") ProductStatus valid, Pageable pageable);

	@Query("SELECT p FROM ProductEntity p WHERE p.type IN :types AND p.newPrice BETWEEN :minPrice AND :maxPrice AND p.valid = :valid")
	Page<ProductEntity> findByTypesAndPriceRange(@Param("types") List<String> types, @Param("minPrice") Long minPrice,
			@Param("maxPrice") Long maxPrice,@Param("valid") ProductStatus valid, Pageable pageable);
}
