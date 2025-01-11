package com.userAuth.userAuth.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class AuthException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer errorCode;

    private String message;

    public AuthException(String message) {
        super(message);
        this.message = message;
    }

    public AuthException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public AuthException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getCode();
        this.message = message;
    }

    public AuthException(Integer errorCode, Exception ex) {
        super(ex);
        this.errorCode = errorCode;
        this.message = ex.getMessage();
    }

    public AuthException(Exception ex) {
        super(ex);
        this.message = ex.getMessage();
    }

}
