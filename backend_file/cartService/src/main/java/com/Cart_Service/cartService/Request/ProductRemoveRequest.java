package com.Cart_Service.cartService.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRemoveRequest {
    private String userEmail;
    private Integer productId;
}
