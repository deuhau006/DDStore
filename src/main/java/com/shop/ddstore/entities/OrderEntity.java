package com.shop.ddstore.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@CreatedDate
    private Date createdDate;
	
	private String username;
	
	private String paymentStatus;
	
	private String billingAddress;
	
	private String phone;
	
	private OrderStatus orderStatus;
	
	private double orderAmt;
	
	private double totalPricesOrder;
	
	@Column(columnDefinition = "BINARY(16)")
	private UUID trackingUUID;
	
	@OneToOne
    @JoinColumn(name = "user_id")
	@JsonBackReference
    private UserEntity user;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonManagedReference
    private Set<OrderItemEntity> orderItems = new HashSet<>();
}
