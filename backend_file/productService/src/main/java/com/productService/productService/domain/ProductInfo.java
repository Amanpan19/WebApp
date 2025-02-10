package com.productService.productService.domain;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProductInfo extends BaseEntity{
    private String clothType; // clothe type : Cotton etc.
    private List<String>availableSize;
    private boolean return14DayAvailability;
    private List<String> colorsAvail;
    private String fashionType; // casual
    private List<String> proTags;
}
