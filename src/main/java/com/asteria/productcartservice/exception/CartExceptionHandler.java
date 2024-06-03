package com.asteria.productcartservice.exception;

import com.asteria.productcartservice.cart.model.CartErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CartExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CartErrorResponse> handleProductNotFoundException(EntityNotFoundException ex) {
        CartErrorResponse errorResponse = new CartErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CartErrorResponse> handleProductNotFoundException(IllegalArgumentException ex) {
        CartErrorResponse errorResponse = new CartErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
