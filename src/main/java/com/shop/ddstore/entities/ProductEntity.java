package com.shop.ddstore.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@CreatedDate
	private Date createdDate;
	
    private String name;
    
    private String type;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "oldPrice")
    private Long oldPrice;
    
    @Column(name = "newPrice")
    private Long newPrice;
    
    private double sale;
    
    private double rating;
    
    private ProductStatus valid;
    
    private String imageUrl;
    
    private String imageId;
    
    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CommentEntity> comments = new ArrayList<>();
}

