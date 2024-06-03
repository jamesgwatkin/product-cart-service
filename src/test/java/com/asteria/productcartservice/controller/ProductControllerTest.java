package com.asteria.productcartservice.controller;

import com.asteria.productcartservice.product.model.Product;
import com.asteria.productcartservice.product.model.ProductId;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductControllerTest {

    private static final String PRODUCT_NAME_ONE = "Product One";
    private static final String PRODUCT_DESCRIPTION_ONE = "Product Description One";
    private static final BigDecimal PRODUCT_PRICE_ONE = BigDecimal.valueOf(19.99);

    @Autowired
    ProductController productController;

    static Product product;

    @BeforeAll
    static void setUp() {
        product = new Product();
        product.setName(PRODUCT_NAME_ONE);
        product.setDescription(PRODUCT_DESCRIPTION_ONE);
        product.setPrice(PRODUCT_PRICE_ONE);
    }

    @Test
    void productJourney() {
        ResponseEntity<ProductId> createResponse = productController.createProduct(product);
        assertNotNull(createResponse);
        assertNotNull(createResponse.getBody());

        Long productId = createResponse.getBody().getId(); // Intermediate result

        ResponseEntity<Product> firstGetResponse = productController.getProduct(productId);
        assertNotNull(firstGetResponse);
        assertNotNull(firstGetResponse.getBody());
        Product retreivedProduct = firstGetResponse.getBody();
        assertNotNull(retreivedProduct);
        assertEquals(PRODUCT_NAME_ONE, retreivedProduct.getName());
        assertEquals(PRODUCT_DESCRIPTION_ONE, retreivedProduct.getDescription());
        assertEquals(PRODUCT_PRICE_ONE, retreivedProduct.getPrice());

        Product dataToUpdate = new Product();
        dataToUpdate.setName(PRODUCT_NAME_ONE);
        dataToUpdate.setDescription("New Description");
        dataToUpdate.setPrice(PRODUCT_PRICE_ONE);
        productController.updateProduct(productId, dataToUpdate);
        ResponseEntity<Product> secondGetResponse = productController.getProduct(productId);
        assertNotNull(secondGetResponse);
        assertNotNull(secondGetResponse.getBody());
        Product updatedProduct = secondGetResponse.getBody();
        assertNotNull(updatedProduct);
        assertEquals("New Description", updatedProduct.getDescription());

        productController.deleteProduct(createResponse.getBody().getId());
        assertThrows(EntityNotFoundException.class, () -> productController.getProduct(productId));
    }
}