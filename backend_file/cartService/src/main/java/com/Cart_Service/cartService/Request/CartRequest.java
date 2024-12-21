package com.Cart_Service.cartService.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
    private Integer productId;
    private Integer proQty;
}
