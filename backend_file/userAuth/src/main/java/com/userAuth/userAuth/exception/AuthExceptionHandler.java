package com.userAuth.userAuth.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice
@RestController
public class AuthExceptionHandler {

    @ExceptionHandler(value = AuthException.class)
    public ErrorResponse handleContentNotFoundException(AuthException e, HttpServletResponse response) {
        response.setStatus(e.getErrorCode());
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        error.setErrorCode(e.getErrorCode());
        error.setSuccess(false);
        return error;
    }


}