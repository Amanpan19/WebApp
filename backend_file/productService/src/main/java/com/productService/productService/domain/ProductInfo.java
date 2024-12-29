package com.productService.productService.domain;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProductInfo extends BaseEntity{
    private String clothType;
    private List<String>availableSize;
    private boolean return14DayAvailability;
    private List<String> colorsAvail;
    private String fashionType;
}
