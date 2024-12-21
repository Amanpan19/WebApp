package com.productService.productService.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProductTrending {
    @Id
    private String id;
    private int productId;
    private String category;
    private String productName;
    private String vendor;
    private LocalDate publishedAt;
    private double productPrice;
    private byte[] imageUrl;
    private double productRating;
    private String description;
}
