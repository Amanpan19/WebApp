package com.productService.productService.repository;

import com.productService.productService.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    Product findByProductName(String productName);

    Optional<Product> findByProductId(int productId);

    Page<Product> findAllByOrderByDayViewsDesc(Pageable pageable);

    List<Product> findByActiveTrueAndDeletedFalse();

    @Query( value = "{$and: [" +
            "{'category': {$eq:?0}}, " +
            "{'productPrice': {}}" +
            "{'productDetails.clothType': {$eq:?1}}" +
            "]}")
    Page<Product> findByCategoryAndProductDetails_clothType( String category, String clothType,Pageable pageable);

    @Query("{ 'productDetails.proTags': { $regex: ?0, $options: 'i' } }")
    Page<Product> findByProTags( String proTags,Pageable pageable);
}
