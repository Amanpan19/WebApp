package com.Cart_Service.cartService.Exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavouriteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer errorCode;

    private String message;

    public FavouriteException(String message) {
        super(message);
        this.message = message;
    }

    public FavouriteException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public FavouriteException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getCode();
        this.message = message;
    }

    public FavouriteException(Integer errorCode, Exception ex) {
        super(ex);
        this.errorCode = errorCode;
        this.message = ex.getMessage();
    }

    public FavouriteException(Exception ex) {
        super(ex);
        this.message = ex.getMessage();
    }
}
