package com.shop.ddstore.dto;

import com.shop.ddstore.entities.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	private Long id;
	private Date createdDate;
    private String name;
    private String type;
    private String description;
    private Long oldPrice;
    private Long newPrice;
    private double sale;
    private double rating;
    private String imageUrl;
    private String imageId;
    private ProductStatus valid;
}
