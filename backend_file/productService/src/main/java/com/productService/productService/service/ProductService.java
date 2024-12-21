package com.productService.productService.service;


import com.productService.productService.domain.Product;
import com.productService.productService.exception.ProductAlreadyExistsException;
import com.productService.productService.exception.ProductNotFoundException;
import com.productService.productService.response.ProductTrending;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product addProducts(Product product) throws ProductAlreadyExistsException;

    List<Product> getAllProducts() throws ProductNotFoundException;

    Product getByName(String productName);

    Product getById(int productId) throws ProductNotFoundException;

    List<Product> getProductByCategory(String category);

    Product updateProduct(Product product, int productId);

    Page<Product> getProducts(int pageNum, int pageSize);

    boolean deleteById(int productId);

    Page<ProductTrending>getTrendingProducts(int pageNum, int pageSize);

    void incrementViewCount(Integer productId) throws ProductNotFoundException;

    void incrementDayViewCount(Integer productId) throws ProductNotFoundException;
}
