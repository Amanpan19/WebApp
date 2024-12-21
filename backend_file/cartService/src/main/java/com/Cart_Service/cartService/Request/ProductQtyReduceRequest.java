package com.Cart_Service.cartService.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductQtyReduceRequest {
    private int productId;
    private int proQty;
}
