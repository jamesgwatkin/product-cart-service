package com.asteria.productcartservice.facade.impl;

import com.asteria.productcartservice.product.model.Product;
import com.asteria.productcartservice.product.model.ProductId;
import com.asteria.productcartservice.repository.entity.ProductEntity;
import com.asteria.productcartservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductFacadeImplTest {

    public static final String TEST_PRODUCT = "Test Product";
    public static final String TEST_DESCRIPTION = "Test Description";
    public static final BigDecimal BIG_DECIMAL = BigDecimal.valueOf(10.00);
    @Mock
    private ProductService productService;

    @Mock
    private ProductEntity productEntity;

    @InjectMocks
    private ProductFacadeImpl productFacade;

    @Test
    void createProduct() {
        when(productEntity.getId()).thenReturn(1L);
        when(productService.createProduct(anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(productEntity);

        ProductId result = productFacade.createProduct(TEST_PRODUCT, TEST_DESCRIPTION, BIG_DECIMAL);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productService, times(1)).createProduct(TEST_PRODUCT, TEST_DESCRIPTION, BIG_DECIMAL);
    }

    @Test
    void deleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        productFacade.deleteProduct(1L);

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void getProduct() {
        when(productEntity.getName()).thenReturn(TEST_PRODUCT);
        when(productEntity.getDescription()).thenReturn(TEST_DESCRIPTION);
        when(productEntity.getPrice()).thenReturn(BIG_DECIMAL);
        when(productService.getProductById(1L)).thenReturn(productEntity);

        Product result = productFacade.getProduct(1L);

        assertNotNull(result);
        assertEquals(TEST_PRODUCT, result.getName());
        assertEquals(TEST_DESCRIPTION, result.getDescription());
        assertEquals(BIG_DECIMAL, result.getPrice());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void updateProduct() {
        when(productService.getProductById(1L)).thenReturn(productEntity);
        when(productService.updateProduct(any(ProductEntity.class))).thenReturn(new ProductEntity());

        productFacade.updateProduct(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(20.00));

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).updateProduct(any(ProductEntity.class));
    }
}