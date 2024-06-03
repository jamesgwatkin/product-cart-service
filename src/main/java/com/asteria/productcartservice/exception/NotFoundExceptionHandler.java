package com.asteria.productcartservice.exception;

import com.asteria.productcartservice.cart.model.CartErrorResponse;
import com.asteria.productcartservice.cart.model.InvalidQuantityErrorResponse;
import com.asteria.productcartservice.product.model.ProductErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ProductErrorResponse errorResponse = new ProductErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<InvalidQuantityErrorResponse> handleProductNotFoundException(IllegalArgumentException ex) {
        InvalidQuantityErrorResponse errorResponse = new InvalidQuantityErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<CartErrorResponse> handleCartNotFoundException(CartNotFoundException ex) {
        CartErrorResponse errorResponse = new CartErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
