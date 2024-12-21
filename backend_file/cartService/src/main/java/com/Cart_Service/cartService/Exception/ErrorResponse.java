package com.Cart_Service.cartService.Exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private int errorCode;

    private String message;

    private Boolean success;

    private Object errorData;
}