package com.shop.ddstore.controllers;

import com.shop.ddstore.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class UserProductController {

	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findProductById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(productServiceImpl.getOne(id));
	}

	@GetMapping("/list")
	public ResponseEntity<?> getAllProducts(
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {
		return productServiceImpl.getAll(pageNo, pageSize, sortBy);
	}

	@GetMapping("/filter/type/{types}")
	public ResponseEntity<?> filterByType(@PathVariable("types") List<String> types,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sort", defaultValue = "0", required = false) int sort) {
		return productServiceImpl.filterByType(types, pageNo, pageSize, sort);
	}

	@GetMapping("/top/{type}")
	public ResponseEntity<?> getTop5(@PathVariable("type") String type) {
		return productServiceImpl.getTop5ProductByType(type);
	}

	@GetMapping("/find/{name}")
	public ResponseEntity<?> findByName(@PathVariable("name") String name) {
		return productServiceImpl.findByName(name);
	}

	@GetMapping("/find/")
	public ResponseEntity<?> findByTypeAndPriceRange(
			@RequestParam(value = "types", required = false) List<String> types,
			@RequestParam(value = "minPrice", defaultValue = "0", required = false) Long minPrice,
			@RequestParam(value = "maxPrice", required = false) Long maxPrice,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "1", required = false) int pageSize,
			@RequestParam(value = "sort", defaultValue = "0", required = false) int sort,
			@RequestParam(value = "sortBy", defaultValue = "newPrice", required = false) String sortBy) {
		return productServiceImpl.findByTypeAndPriceRange(types, minPrice, maxPrice, pageNo, pageSize, sort, sortBy);
	}
}
