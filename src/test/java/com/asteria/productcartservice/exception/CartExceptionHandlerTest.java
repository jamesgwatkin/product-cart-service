package com.asteria.productcartservice.exception;

import com.asteria.productcartservice.cart.model.CartErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CartExceptionHandlerTest {

    public static final String MESSAGE = "Error";
    CartExceptionHandler cartExceptionHandler = new CartExceptionHandler();

    @Test
    void handleProductNotFoundExceptionForEntityNotFoundException() {
        ResponseEntity<CartErrorResponse> response = cartExceptionHandler.handleProductNotFoundException(new EntityNotFoundException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        CartErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void handleProductNotFoundExceptionForIllegalArgumentException() {
        ResponseEntity<CartErrorResponse> response = cartExceptionHandler.handleProductNotFoundException(new IllegalArgumentException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        CartErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }
}