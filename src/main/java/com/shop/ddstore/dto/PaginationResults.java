package com.shop.ddstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResults<T> {

	private List<T> data;
    private int totalPages;
    private Long totalItems;
}
