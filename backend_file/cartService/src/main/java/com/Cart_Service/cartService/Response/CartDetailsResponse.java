package com.Cart_Service.cartService.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CartDetailsResponse {

    private String userId;
    private Map<Integer, Map<String, Object>> productDetails;
    private int noOfProducts;
}
