package com.productService.productService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Product {
    @Id
    private String id;
    private int productId;
    private String category;
    private String productName;
    private int quantity;
    private String vendor;
    private LocalDate publishedAt;
    private double productPrice;
    private byte[] imageUrl;
    private double productRating;
    private String description;
    private int views;
    private int dayViews;
}
