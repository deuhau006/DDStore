package com.shop.ddstore.controllers;

import com.shop.ddstore.dto.ProductDTO;
import com.shop.ddstore.entities.ProductStatus;
import com.shop.ddstore.exception.ProductException;
import com.shop.ddstore.service.CloudinaryService;
import com.shop.ddstore.service.impl.OrderServiceImpl;
import com.shop.ddstore.service.impl.ProductServiceImpl;
import com.shop.ddstore.service.impl.UserServiceImpl;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
public class AdminController {

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private OrderServiceImpl orderService;

	@GetMapping("/list")
	public ResponseEntity<?> getAllProducts(
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {
		return productServiceImpl.getAll(pageNo, pageSize, sortBy);
	}

	@GetMapping("/filter/type/{types}")
	public ResponseEntity<?> filterByType(@PathVariable("type") List<String> types,
			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sort", defaultValue = "0", required = false) int sort) {
		return productServiceImpl.filterByType(types, pageNo, pageSize, sort);
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "size", defaultValue = "10", required = false) int pageSize) {
		return userService.getUsers(pageNo, pageSize);
	}

	@GetMapping("/getAllOrders")
	public ResponseEntity<?> getAllOrders(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "size", defaultValue = "10", required = false) int pageSize) {
		return orderService.getAllOrders(pageNo, pageSize);
	}

	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<?> createNewProduct(@ModelAttribute ProductDTO productDTO,
			@RequestParam(value = "file") MultipartFile file) throws IOException {
		BufferedImage bi = ImageIO.read(file.getInputStream());
		if (bi == null) {
			return ResponseEntity.ok("Image not valid!");
		}
		Map result = cloudinaryService.upload(file);
		ProductDTO dto = new ProductDTO();
		dto.setName(productDTO.getName());
		dto.setType(productDTO.getType());
		dto.setDescription(productDTO.getDescription());
		dto.setOldPrice(productDTO.getOldPrice());
		dto.setSale(productDTO.getSale());
		dto.setNewPrice(productDTO.getNewPrice());
		dto.setRating(productDTO.getRating());
		dto.setImageUrl((String) result.get("url"));
		dto.setImageId((String) result.get("public_id"));
		dto.setValid(ProductStatus.Available);
		productServiceImpl.save(dto);
		return ResponseEntity.ok("Created successfully!");
	}

	@PostMapping("/update/{id}")
	@ResponseBody
	public ResponseEntity<?> updateProduct(@ModelAttribute ProductDTO productDTO, @PathVariable String id) {
		ProductDTO dto = new ProductDTO();

		long idProduct = Long.parseLong(id);

		ProductDTO old = productServiceImpl.getOne(idProduct);

		dto.setId(idProduct);
		dto.setName(productDTO.getName());
		dto.setType(productDTO.getType());
		dto.setDescription(productDTO.getDescription());
		dto.setOldPrice(productDTO.getOldPrice());
		dto.setSale(productDTO.getSale());
		dto.setNewPrice(productDTO.getNewPrice());
		dto.setRating(productDTO.getRating());
		dto.setImageUrl(old.getImageUrl());
		dto.setImageId(old.getImageId());

		productServiceImpl.update(dto);
		return ResponseEntity.ok("Update successfully!");
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		try {
			return ResponseEntity.ok(productServiceImpl.delete(id));
		} catch (ProductException e) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Not found");
		}
	}

	@GetMapping("/getTotalPricesByRange")
	public ResponseEntity<?> getTotalPriceByRange(@RequestParam("startDate") String stStartDate,
			@RequestParam("endDate") String stEndDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String start = stStartDate;
		String end = stEndDate;

		Date startDate = dateFormat.parse(start);
		Date endDate = dateFormat.parse(end);

		return orderService.getTotalPriceInDateRange(startDate, endDate);
	}

	@GetMapping("/getTotalPriceInYear/{year}")
	public ResponseEntity<?> getTotalPriceInYear(@PathVariable("year") int year) {
		return orderService.getTotalPriceByMonthInYear(year);
	}

	@GetMapping("/findOrderById/{id}")
	public ResponseEntity<?> getOneOrder(@PathVariable("id") Long orderId) {
		return orderService.findOrderById(orderId);
	}

	@PutMapping("/updateOrderStatus/{id}/{status}")
	public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Long orderId, @PathVariable("status") int status) {
		return orderService.updateOrderStatus(orderId, status);
	}
	
	@GetMapping("/count/orderStatus")
	public ResponseEntity<?> countOrderStatus() {
		return ResponseEntity.ok(orderService.countOrdersByStatus());
	}
}
