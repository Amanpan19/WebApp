package com.productService.productService.response;

import com.productService.productService.domain.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryResponse {
    private int productId;
    private String category;
    private String productName;
    private double productPrice;
    private byte[] imageUrl;
    private double productRating;
    private ProductInfo productDetails;
}
