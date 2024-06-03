package com.asteria.productcartservice.controller;

import com.asteria.productcartservice.cart.model.Cart;
import com.asteria.productcartservice.cart.model.CartId;
import com.asteria.productcartservice.cart.model.CartLineItemsInner;
import com.asteria.productcartservice.cart.model.Product;
import com.asteria.productcartservice.product.model.ProductId;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CartControllerTest {

    private static final String PRODUCT_NAME_ONE = "Product One";
    private static final String PRODUCT_DESCRIPTION_ONE = "Product Description One";
    private static final BigDecimal PRODUCT_PRICE_ONE = BigDecimal.valueOf(19.99);
    private static final String PRODUCT_NAME_TWO = "Product Two";
    private static final String PRODUCT_DESCRIPTION_TWO = "Product Description Two";
    private static final BigDecimal PRODUCT_PRICE_TWO = BigDecimal.valueOf(10.99);

    @Autowired
    ProductController productController;
    @Autowired
    private CartController cartController;

    Long productIdOne;
    Long productIdTwo;

    @BeforeEach
    void setUp() {
        com.asteria.productcartservice.product.model.Product productOne
                = new com.asteria.productcartservice.product.model.Product();
        productOne.setName(PRODUCT_NAME_ONE);
        productOne.setDescription(PRODUCT_DESCRIPTION_ONE);
        productOne.setPrice(PRODUCT_PRICE_ONE);
        ResponseEntity<ProductId> productIdOneResponse = productController.createProduct(productOne);
        productIdOne = Objects.requireNonNull(productIdOneResponse.getBody()).getId();

        com.asteria.productcartservice.product.model.Product productTwo
                = new com.asteria.productcartservice.product.model.Product();
        productTwo.setName(PRODUCT_NAME_TWO);
        productTwo.setDescription(PRODUCT_DESCRIPTION_TWO);
        productTwo.setPrice(PRODUCT_PRICE_TWO);
        ResponseEntity<ProductId> productIdTwoResponse = productController.createProduct(productTwo);
        productIdTwo = Objects.requireNonNull(productIdTwoResponse.getBody()).getId();
    }

    @Test
    void cartJourny() {
        ResponseEntity<CartId> cartIdResponse = cartController.createCart();
        assertThat(cartIdResponse.getBody()).isNotNull();
        Long cartId = cartIdResponse.getBody().getId();

        cartController.addProductToCart(cartId, productIdOne, 2);

        ResponseEntity<Cart> getCartResponse = cartController.getCart(cartId);
        assertThat(getCartResponse.getBody()).isNotNull();
        Cart cart = getCartResponse.getBody();
        assertThat(cart.getLineItems()).hasSize(1);
        CartLineItemsInner lineItemOne = cart.getLineItems().getFirst();
        assertThat(lineItemOne.getQuantity()).isEqualTo(2);
        assertThat(lineItemOne.getProduct().getName()).isEqualTo(PRODUCT_NAME_ONE);

        cartController.addProductToCart(cartId, productIdTwo, 4);

        getCartResponse = cartController.getCart(cartId);
        assertThat(getCartResponse.getBody()).isNotNull();
        cart = getCartResponse.getBody();
        assertThat(cart.getLineItems()).hasSize(2);
        boolean foundProductOne = false;
        boolean foundProductTwo = false;

        for (CartLineItemsInner lineItem : cart.getLineItems()) {
            if (PRODUCT_NAME_ONE.equals(lineItem.getProduct().getName())) {
                assertThat(lineItem.getQuantity()).isEqualTo(2);
                foundProductOne = true;
            } else if (PRODUCT_NAME_TWO.equals(lineItem.getProduct().getName())) {
                assertThat(lineItem.getQuantity()).isEqualTo(4);
                foundProductTwo = true;
            }
        }
        assertThat(foundProductOne).isTrue();
        assertThat(foundProductTwo).isTrue();

        ResponseEntity<List<Product>> productsResponse = cartController.getCartProducts(cartId);
        assertThat(productsResponse.getBody()).isNotNull();
        List<Product> products = productsResponse.getBody();
        assertThat(products).hasSize(2);

        foundProductOne = false;
        foundProductTwo = false;

        for (Product product : products) {
            if (PRODUCT_NAME_ONE.equals(product.getName())) {
                foundProductOne = true;
            } else if (PRODUCT_NAME_TWO.equals(product.getName())) {
                foundProductTwo = true;
            }
        }
        assertThat(foundProductOne).isTrue();
        assertThat(foundProductTwo).isTrue();

        cartController.removeProductFromCart(cartId, productIdTwo, 2);
        getCartResponse = cartController.getCart(cartId);
        assertThat(getCartResponse.getBody()).isNotNull();
        cart = getCartResponse.getBody();
        assertThat(cart.getLineItems()).hasSize(2);
        for (CartLineItemsInner lineItem : cart.getLineItems()) {
            if (PRODUCT_NAME_ONE.equals(lineItem.getProduct().getName())) {
                assertThat(lineItem.getQuantity()).isEqualTo(2);
            } else if (PRODUCT_NAME_TWO.equals(lineItem.getProduct().getName())) {
                assertThat(lineItem.getQuantity()).isEqualTo(2);
            }
        }

        cartController.clearCart(cartId);
        getCartResponse = cartController.getCart(cartId);
        assertThat(getCartResponse.getBody()).isNotNull();
        cart = getCartResponse.getBody();
        assertThat(cart.getLineItems()).isEmpty();

        assertThrows(EntityNotFoundException.class, () -> cartController.getCart(cartId + 2));
        assertThrows(EntityNotFoundException.class, () -> cartController.addProductToCart(cartId, productIdOne + productIdTwo, 2));
        assertThrows(EntityNotFoundException.class, () -> cartController.removeProductFromCart(cartId, productIdOne + productIdTwo, 2));
        assertThrows(EntityNotFoundException.class, () -> cartController.clearCart(cartId + 2));

    }
}
