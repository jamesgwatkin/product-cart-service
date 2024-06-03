package com.asteria.productcartservice.exception;

import com.asteria.productcartservice.cart.model.CartErrorResponse;
import com.asteria.productcartservice.cart.model.InvalidQuantityErrorResponse;
import com.asteria.productcartservice.product.model.ProductErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionHandlerTest {

    public static final String MESSAGE = "Error";
    NotFoundExceptionHandler notFoundExceptionHandler = new NotFoundExceptionHandler();

    @Test
    void handleProductNotFoundExceptionForEntityNotFoundException() {
        ResponseEntity<ProductErrorResponse> response = notFoundExceptionHandler.handleProductNotFoundException(new ProductNotFoundException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        ProductErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void handleProductNotFoundExceptionForIllegalArgumentException() {
        ResponseEntity<InvalidQuantityErrorResponse> response = notFoundExceptionHandler.handleProductNotFoundException(new IllegalArgumentException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        InvalidQuantityErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void handleCartNotFoundExceptionForIllegalArgumentException() {
        ResponseEntity<CartErrorResponse> response = notFoundExceptionHandler.handleCartNotFoundException(new CartNotFoundException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        CartErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }
}