package com.productService.productService.repository;

import com.productService.productService.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    Product findByProductName(String productName);
    Optional<Product> findByProductId(int productId);
    Page<Product> findAllByOrderByDayViewsDesc(Pageable pageable);

}
