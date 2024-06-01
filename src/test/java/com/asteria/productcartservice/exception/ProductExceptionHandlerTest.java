package com.asteria.productcartservice.exception;

import com.asteria.productcartservice.product.model.ProductErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductExceptionHandlerTest {

    public static final String MESSAGE = "Error";
    ProductExceptionHandler productExceptionHandler = new ProductExceptionHandler();

    @Test
    void handleProductNotFoundExceptionForEntityNotFoundException() {
        ResponseEntity<ProductErrorResponse> response = productExceptionHandler.handleProductNotFoundException(new EntityNotFoundException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        ProductErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void handleProductNotFoundExceptionForIllegalArgumentException() {
        ResponseEntity<ProductErrorResponse> response = productExceptionHandler.handleProductNotFoundException(new IllegalArgumentException(MESSAGE));
        assertNotNull(response);
        assertNotNull(response.getBody());
        ProductErrorResponse error = response.getBody();
        assertThat(error.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error.getMessage()).isEqualTo(MESSAGE);
    }
}