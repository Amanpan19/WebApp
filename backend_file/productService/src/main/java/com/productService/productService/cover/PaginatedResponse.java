package com.productService.productService.cover;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

	private long totalElements;

	private long totalPages;

	private long pageSize;

	private long pageNumber;

	private long numberOfElements;

	private List<T> data;
}