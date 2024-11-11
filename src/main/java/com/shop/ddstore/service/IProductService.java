package com.shop.ddstore.service;

import com.shop.ddstore.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductService {

	ResponseEntity<?> save(ProductDTO productDTO);
	
	ResponseEntity<?> update(ProductDTO productDTO);

	ProductDTO getOne(long id);

	ResponseEntity<?> getAll(int pageNo, int pageSize, String sortBy);

	ResponseEntity<?> delete(long id);

	ResponseEntity<?> findByName(String name);

	ResponseEntity<?> filterByType(List<String> types, int pageNo, int pageSize, int sort);

	ResponseEntity<?> getTop5ProductByType(String type);

	ResponseEntity<?> findByTypeAndPriceRange(List<String> types, Long minPrice, Long maxPrice, int pageNo,
			int pageSize,int sort, String sortBy);
}
