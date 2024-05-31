package com.asteria.productcartservice.service;

import com.asteria.productcartservice.repository.entity.ProductEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceTest {

    private static final String PRODUCT_NAME_ONE = "Product One";
    private static final String PRODUCT_NAME_TWO = "Product Two";

    private static final String PRODUCT_DESCRIPTION_ONE = "Product Description One";
    private static final String PRODUCT_DESCRIPTION_TWO = "Product Description Two";

    private static final BigDecimal PRODUCT_PRICE_ONE = BigDecimal.valueOf(19.99);
    private static final BigDecimal PRODUCT_PRICE_TWO = BigDecimal.valueOf(8.99);

    @Autowired
    private ProductService productService;

    @Test
    void testCreateProduct() {
        ProductEntity savedProduct = productService.createProduct(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(PRODUCT_NAME_ONE);
        assertThat(savedProduct.getDescription()).isEqualTo(PRODUCT_DESCRIPTION_ONE);
        assertThat(savedProduct.getPrice()).isEqualTo(PRODUCT_PRICE_ONE);
    }

    @Test
    void testUpdateProduct() {
        ProductEntity savedProduct = productService.createProduct(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getPrice()).isEqualTo(PRODUCT_PRICE_ONE);

        savedProduct.setName(PRODUCT_NAME_TWO);
        ProductEntity updatedProduct = productService.updateProduct(savedProduct);
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getName()).isEqualTo(PRODUCT_NAME_TWO);
        assertThat(updatedProduct.getDescription()).isEqualTo(PRODUCT_DESCRIPTION_ONE);
        assertThat(updatedProduct.getPrice()).isEqualTo(PRODUCT_PRICE_ONE);
    }

    @Test
    void testUpdateProductWithoutId() {
        ProductEntity product = new ProductEntity(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(product));
    }

    @Test
    void getProductById() {
        ProductEntity savedProductOne = productService.createProduct(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);
        ProductEntity savedProductTwo = productService.createProduct(PRODUCT_NAME_TWO, PRODUCT_DESCRIPTION_TWO, PRODUCT_PRICE_TWO);

        Optional<ProductEntity> retreivedProductOne = productService.getProductById(savedProductOne.getId());
        assertThat(retreivedProductOne).isPresent();
        assertThat(retreivedProductOne.get().getName()).isEqualTo(PRODUCT_NAME_ONE);
        assertThat(retreivedProductOne.get().getDescription()).isEqualTo(PRODUCT_DESCRIPTION_ONE);
        assertThat(retreivedProductOne.get().getPrice()).isEqualTo(PRODUCT_PRICE_ONE);

        Optional<ProductEntity> retreivedProductTwo = productService.getProductById(savedProductTwo.getId());
        assertThat(retreivedProductTwo).isPresent();
        assertThat(retreivedProductTwo.get().getName()).isEqualTo(PRODUCT_NAME_TWO);
        assertThat(retreivedProductTwo.get().getDescription()).isEqualTo(PRODUCT_DESCRIPTION_TWO);
        assertThat(retreivedProductTwo.get().getPrice()).isEqualTo(PRODUCT_PRICE_TWO);
    }

    @Test
    void getNonExistentProduct() {
        Optional<ProductEntity> nonExistentProduct = productService.getProductById(1L);
        assertThat(nonExistentProduct).isNotPresent();
    }

    @Test
    void getAllProducts() {
        productService.createProduct(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);
        productService.createProduct(PRODUCT_NAME_TWO, PRODUCT_DESCRIPTION_TWO, PRODUCT_PRICE_TWO);

        List<ProductEntity> allProducts = productService.getAllProducts();
        assertThat(allProducts).isNotEmpty().hasSize(2);
    }

    @Test
    void deleteProduct() {
        productService.createProduct(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);
        ProductEntity productTwo = productService.createProduct(PRODUCT_NAME_TWO, PRODUCT_DESCRIPTION_TWO, PRODUCT_PRICE_TWO);

        productService.deleteProduct(productTwo.getId());

        List<ProductEntity> allProducts = productService.getAllProducts();
        assertThat(allProducts)
                .isNotEmpty()
                .hasSize(1)
                .extracting("name", "description", "price")
                .containsExactly(
                        tuple(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE)
                );
    }

    @Test
    void deleteNonExistentProduct() {
        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}