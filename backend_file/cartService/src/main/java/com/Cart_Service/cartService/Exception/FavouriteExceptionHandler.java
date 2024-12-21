package com.Cart_Service.cartService.Exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class FavouriteExceptionHandler {

    @ExceptionHandler(value = FavouriteException.class)
    public ErrorResponse handleContentNotFoundException(FavouriteException e, HttpServletResponse response) {
        response.setStatus(e.getErrorCode());
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        error.setErrorCode(e.getErrorCode());
        error.setSuccess(false);
        return error;
    }

}
