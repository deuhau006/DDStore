package com.shop.ddstore.service.impl;

import com.shop.ddstore.converter.OrderConverter;
import com.shop.ddstore.dto.OrderDTO;
import com.shop.ddstore.dto.OrderStatusSummary;
import com.shop.ddstore.dto.PaginationResults;
import com.shop.ddstore.dto.request.GetOrderRequest;
import com.shop.ddstore.dto.request.PlaceOrderRequest;
import com.shop.ddstore.entities.*;
import com.shop.ddstore.repositories.OrderRepository;
import com.shop.ddstore.repositories.ShoppingCartRepository;
import com.shop.ddstore.repositories.UserRepository;
import com.shop.ddstore.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ShoppingCartRepository shoppingCartRepository;

	@Override
	@Transactional
	public OrderEntity orderCreate(PlaceOrderRequest request, String email) {
		UserEntity user = userRepository.findByEmail(email).get();
		ShoppingCartEntity shoppingCart = user.getCart();
		OrderEntity orderEntity = new OrderEntity();

		Set<OrderItemEntity> orderItemsEntity = new HashSet<>();
		Set<CartItemEntity> cartItemsEntity = new HashSet<>(shoppingCart.getCartItems());
		for (CartItemEntity cartItem : cartItemsEntity) {
			OrderItemEntity orderItem = new OrderItemEntity();
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getTotalPrice());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(orderEntity);
			orderItemsEntity.add(orderItem);
			orderEntity.setOrderAmt(orderEntity.getOrderAmt() + cartItem.getQuantity());
		}

		orderEntity.setUsername(request.getUsername());
		orderEntity.setBillingAddress(request.getAddress());
		orderEntity.setOrderStatus(OrderStatus.PENDING);
		orderEntity.setUser(user);
		orderEntity.setPaymentStatus(request.getPayment());
		orderEntity.setOrderItems(orderItemsEntity);
		orderEntity.setTrackingUUID(UUID.randomUUID());
		orderEntity.setPhone(request.getPhone());
		orderEntity.setTotalPricesOrder(shoppingCart.getTotalPrice());
		orderRepository.save(orderEntity);

		shoppingCartRepository.flush();

		shoppingCart.getCartItems().clear();
		shoppingCart.setTotalItems(0);
		shoppingCart.setTotalPrice(0);
		shoppingCartRepository.save(shoppingCart);

		return orderEntity;
	}

	@Override
	public ResponseEntity<?> findOrderByUUID(String trackingUUID) {
		UUID uuid = UUID.fromString(trackingUUID);
        OrderEntity order = orderRepository.findByTrackingUUID(uuid);
        if (order == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng");
        } else {
        	return ResponseEntity.ok(OrderConverter.toDTO(order));
        }
	}

	@Override
	public ResponseEntity<?> getAllOrderFromUser(String email) {
		UserEntity user = userRepository.findByEmail(email).get();
		if (user != null) {
			List<OrderEntity> listOrders = orderRepository.findByUser(user);
			List<OrderDTO> listOrdersDTO = new ArrayList<>();
			for (OrderEntity entity : listOrders) {
				OrderDTO dto = OrderConverter.toDTO(entity);
				listOrdersDTO.add(dto);
			}
			
			int totalOrders = orderRepository.countByUser(user);
			
			GetOrderRequest<OrderDTO> getOrderByUser = new GetOrderRequest<>();
			getOrderByUser.setData(listOrdersDTO);
			getOrderByUser.setTotalOrders(totalOrders);
			
			return ResponseEntity.ok(getOrderByUser);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy dữ liệu.");
		}
	}

	@Override
	public ResponseEntity<?> getAllOrders(int pageNo, int pageSize) {
		long totalItems = orderRepository.count();
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		
		if (pageNo < 1 || pageNo > totalPages) {
			throw new IllegalArgumentException("Trang không hợp lệ.");
		}
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "createdDate"));
		
		Page<OrderEntity> listOrders = orderRepository.findAll(pageable);
		
		if (listOrders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có đơn hàng nào.");
		} else {
			List<OrderDTO> listOrdersDTO = new ArrayList<>();
			for (OrderEntity entity : listOrders) {
				OrderDTO dto = OrderConverter.toDTO(entity);
				listOrdersDTO.add(dto);
			}
			
			PaginationResults<OrderDTO> paginationResults = new PaginationResults<>();
			paginationResults.setData(listOrdersDTO);
			paginationResults.setTotalItems(totalItems);
			paginationResults.setTotalPages(totalPages);
			
			return ResponseEntity.ok(paginationResults);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateOrderStatus(Long orderId, int status) {
		try {
			OrderEntity order = orderRepository.findById(orderId).get();
			if (status == 1) {
				order.setOrderStatus(OrderStatus.DELIVERED);
			} else {
				order.setOrderStatus(OrderStatus.CANCEL);
			}
			
			return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công.");
		} catch (Exception e) {
			return ResponseEntity.ok("Error" + e);
		}
	}

	@Override
	public ResponseEntity<?> getTotalPriceInDateRange(Date startDate, Date endDate) {
		return ResponseEntity.ok(orderRepository.getTotalPriceInDateRange(startDate, endDate));
	}

	@Override
	public ResponseEntity<?> getTotalPriceByMonthInYear(int year) {
		try {
			List<Object[]> results = orderRepository.getTotalPriceByMonthInYear(year);
			return ResponseEntity.ok(results);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("Error: " + e);
		}
	}

	@Override
	public ResponseEntity<?> getOneOrder(String email, Long orderId) {
		try {
			UserEntity user = userRepository.findByEmail(email).get();
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin khách hàng.");
			}
			OrderEntity order = orderRepository.findById(orderId).get();
			if (order == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin đơn hàng.");
			} else {
				return ResponseEntity.ok(OrderConverter.toDTO(order));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error.");		
		}
	}

	@Override
	public ResponseEntity<?> findOrderById(Long orderId) {
		OrderEntity order = orderRepository.findById(orderId).get();
		return ResponseEntity.ok(OrderConverter.toDTO(order));
	}

	@Override
	public OrderStatusSummary countOrdersByStatus() {
		long pending = orderRepository.countPendingOrders();
		long delivered = orderRepository.countDeliveredOrders();
		long canceled = orderRepository.countCanceledOrders();
		long totalEarn = orderRepository.sumTotalPricesForDeliveredOrders();
		
		OrderStatusSummary result = new OrderStatusSummary();
		result.setPending(pending);
		result.setDelivered(delivered);
		result.setCanceled(canceled);
		result.setTotalEarn(totalEarn);
		return result;
	}
}
