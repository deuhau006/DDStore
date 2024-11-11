package com.shop.ddstore.service.impl;

import com.shop.ddstore.converter.ProductConverter;
import com.shop.ddstore.dto.PaginationResults;
import com.shop.ddstore.dto.ProductDTO;
import com.shop.ddstore.entities.ProductEntity;
import com.shop.ddstore.entities.ProductStatus;
import com.shop.ddstore.repositories.ProductRepository;
import com.shop.ddstore.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ResponseEntity<?> save(ProductDTO productDTO) {
		try {
			ProductEntity entity = new ProductEntity();
			entity = ProductConverter.toEntity(productDTO);
			productRepository.save(entity);

			return ResponseEntity.ok("Thêm sản phẩm mới thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thêm sản phẩm mới thất bại.");
		}
	}
	
	@Override
	public ResponseEntity<?> update(ProductDTO productDTO) {
		try {
			ProductEntity entity = new ProductEntity();
			ProductEntity oldProduct = productRepository.findById(productDTO.getId()).get();
			entity = ProductConverter.toEntity(productDTO, oldProduct);
			entity.setCreatedDate(new Date());
			entity.setValid(ProductStatus.Available);
			productRepository.save(entity);
			
			return ResponseEntity.ok("Cập nhật sản phẩm thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cập nhật sản phẩm không thành công.");
		}
	}

	@Override
	public ResponseEntity<?> delete(long id) {
		try {
			ProductEntity product = productRepository.findById(id).get();
			product.setValid(ProductStatus.Deleted);
			productRepository.save(product);
			return ResponseEntity.ok("Xóa sản phẩm thành công.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa sản phẩm thất bại.");
		}
	}

	@Override
	public ResponseEntity<?> findByName(String name) {
		List<ProductEntity> entities = productRepository.findByNameContainingAndValid(name, ProductStatus.Available);
		
		if (entities.size() == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm.");
		} else {
			List<ProductDTO> results = new ArrayList<>();
			for (ProductEntity item : entities) {
				ProductDTO dto = ProductConverter.toDTO(item);
				results.add(dto);
			}

			return ResponseEntity.ok(results);
		}
	}

	@Override
	public ProductDTO getOne(long id) {
		Optional<ProductEntity> optionalEntity = productRepository.findById(id);
		ProductEntity entity = optionalEntity.get();
		ProductDTO dto = ProductConverter.toDTO(entity);
		return dto;
	}

	@Override
	public ResponseEntity<?> filterByType(List<String> types, int pageNo, int pageSize, int sort) {
		Pageable pageable = null;
		if (sort == 0) {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "newPrice"));
		} else {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "newPrice"));
		}
		Page<ProductEntity> list = productRepository.findByTypeAndValid(types, ProductStatus.Available, pageable);
		List<ProductDTO> results = new ArrayList<>();
		for (ProductEntity item : list) {
			ProductDTO dto = ProductConverter.toDTO(item);
			results.add(dto);
		}
		
		PaginationResults<ProductDTO> paginationResult = new PaginationResults<>();
		paginationResult.setData(results);
		paginationResult.setTotalPages(list.getTotalPages());
		
		return ResponseEntity.ok(paginationResult);
	}

	@Override
	public ResponseEntity<?> getAll(int pageNo, int pageSize, String sortBy) {
		long totalItems = productRepository.count();
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		if (pageNo < 1 || pageNo > totalPages) {
			throw new IllegalArgumentException("Trang không hợp lệ.");
		}

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
		Page<ProductEntity> list = productRepository.findAllValidProducts(ProductStatus.Available, pageable);
		List<ProductDTO> results = new ArrayList<>();
		for (ProductEntity item : list) {
			ProductDTO dto = ProductConverter.toDTO(item);
			results.add(dto);
		}

		PaginationResults<ProductDTO> paginationResult = new PaginationResults<>();
		paginationResult.setData(results);
		paginationResult.setTotalPages(totalPages);
		paginationResult.setTotalItems(totalItems);

		return ResponseEntity.ok(paginationResult);
	}

	@Override
	public ResponseEntity<?> getTop5ProductByType(String type) {
		List<ProductEntity> list = productRepository.findTop5ByTypeAndValidOrderByTypeDesc(type, ProductStatus.Available);
		List<ProductDTO> results = new ArrayList<>();
		for (ProductEntity item : list) {
			ProductDTO dto = ProductConverter.toDTO(item);
			results.add(dto);
		}

		return ResponseEntity.ok(results);
	}

	@Override
	public ResponseEntity<?> findByTypeAndPriceRange(List<String> types, Long minPrice, Long maxPrice,
			int pageNo, int pageSize, int sort, String sortBy) {
		Pageable pageable = null;
		if (sort == 0) {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
		} else {
			pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
		}
		
		Page<ProductEntity> list;
		
		if (types.isEmpty() && maxPrice == 0) {
			list = productRepository.findAllValidProducts(ProductStatus.Available, pageable);
			System.out.println("1");
		} else if (!types.isEmpty() && minPrice > 0 && maxPrice > 0) {
			list = productRepository.findByTypesAndPriceRange(types, minPrice, maxPrice, ProductStatus.Available, pageable);
			System.out.println("3");
		} else if (minPrice == 0 && !types.isEmpty()) {
			list = productRepository.findByTypeAndValid(types, ProductStatus.Available, pageable);
			System.out.println("4");
		} else if (types == null || types.isEmpty() && minPrice == 0) {
			list = productRepository.findByPriceRange(minPrice, maxPrice, ProductStatus.Available, pageable);
			System.out.println("5");
		} else {
			list = productRepository.findByTypeAndMinPrice(types, minPrice, ProductStatus.Available, pageable);
			System.out.println("6");
		}
		
		if (list == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm.");
	    }

		List<ProductDTO> results = new ArrayList<>();
		for (ProductEntity item : list) {
			ProductDTO dto = ProductConverter.toDTO(item);
			results.add(dto);
		}
		
		PaginationResults<ProductDTO> paginationResult = new PaginationResults<>();
		paginationResult.setData(results);
		paginationResult.setTotalPages(list.getTotalPages());

		return ResponseEntity.ok(paginationResult);
	}
}
