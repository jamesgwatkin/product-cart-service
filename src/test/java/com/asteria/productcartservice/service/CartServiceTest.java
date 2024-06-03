package com.asteria.productcartservice.service;

import com.asteria.productcartservice.repository.CartLineItemRepository;
import com.asteria.productcartservice.repository.CartRepository;
import com.asteria.productcartservice.repository.entity.CartEntity;
import com.asteria.productcartservice.repository.entity.CartLineItemEntity;
import com.asteria.productcartservice.repository.entity.ProductEntity;
import jakarta.persistence.EntityNotFoundException;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CartServiceTest {

    private static final String PRODUCT_NAME_ONE = "Product One";
    private static final String PRODUCT_NAME_TWO = "Product Two";

    private static final String PRODUCT_DESCRIPTION_ONE = "Product Description One";
    private static final String PRODUCT_DESCRIPTION_TWO = "Product Description Two";

    private static final BigDecimal PRODUCT_PRICE_ONE = BigDecimal.valueOf(19.99);
    private static final BigDecimal PRODUCT_PRICE_TWO = BigDecimal.valueOf(9.99);

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartLineItemRepository cartLineItemRepository;

    @Autowired
    private CartRepository cartRepository;

    Long productIdOne;
    Long productIdTwo;

    @BeforeEach
    void setUp() {
        ProductEntity productOne = productService.createProduct(PRODUCT_NAME_ONE, PRODUCT_DESCRIPTION_ONE, PRODUCT_PRICE_ONE);
        ProductEntity productTwo = productService.createProduct(PRODUCT_NAME_TWO, PRODUCT_DESCRIPTION_TWO, PRODUCT_PRICE_TWO);
        productIdOne = productOne.getId();
        productIdTwo = productTwo.getId();
    }

    @AfterEach
    void tearDown() {
        cartLineItemRepository.deleteAll();
        cartRepository.deleteAll();
        productService.deleteProduct(productIdOne);
        productService.deleteProduct(productIdTwo);
    }

    @Test
    void testCreateCart() {
        CartEntity cart = cartService.createCart();
        assertNotNull(cart);
        assertNotNull(cart.getId());
        assertEquals(0, cart.getLineItems().size());
    }

    @Test
    void addProductToCart() {
        CartEntity cart = cartService.createCart();
        CartEntity updatedCart = cartService.addProduct(cart, productIdOne, 4);

        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getId()).isEqualTo(cart.getId());
        assertThat(updatedCart.getLineItems()).hasSize(1);

        CartLineItemEntity lineItem = updatedCart.getLineItems().getFirst();
        assertThat(lineItem.getProduct().getId()).isEqualTo(productIdOne);
        assertThat(lineItem.getQuantity()).isEqualTo(4);
    }

    @Test
    void addNonExistentProductToCart() {
        CartEntity cart = cartService.createCart();
        assertThrows(EntityNotFoundException.class, () -> cartService.addProduct(cart, 1L, 4));
    }

    @Test
    void addProductToCartThatIsAlreadyInCart() {
        CartEntity cart = cartService.createCart();
        cartService.addProduct(cart, productIdOne, 4);
        CartEntity updatedCart = cartService.addProduct(cart, productIdOne, 8);
        assertThat(updatedCart.getLineItems()).hasSize(1);

        CartLineItemEntity lineItem = updatedCart.getLineItems().getFirst();
        assertThat(lineItem.getProduct().getId()).isEqualTo(productIdOne);
        assertThat(lineItem.getQuantity()).isEqualTo(12);
    }

    @Test
    void addTwoProductsToCart() {
        CartEntity cart = cartService.createCart();
        cartService.addProduct(cart, productIdOne, 4);
        CartEntity updatedCart = cartService.addProduct(cart, productIdTwo, 4);

        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getLineItems())
                .hasSize(2)
                .extracting(lineItem -> tuple(lineItem.getProduct().getId(), lineItem.getQuantity()))
                .containsExactlyInAnyOrder(
                        tuple(productIdOne, 4),
                        tuple(productIdTwo, 4)
                );
    }

    @Test
    void addProductToCartWithNegativeQuantity() {
        CartEntity cart = cartService.createCart();
        assertThrows(IllegalArgumentException.class, () -> cartService.addProduct(cart, productIdOne, -1));
    }

    @Test
    void removeProductFromCart() {
        CartEntity cart = cartService.createCart();
        CartEntity updatedCart = cartService.addProduct(cart, productIdOne, 4);
        cartService.removeProduct(updatedCart, productIdOne, 2);
        CartLineItemEntity lineItem = updatedCart.getLineItems().getFirst();
        assertThat(lineItem.getQuantity()).isEqualTo(2);
    }

    @Test
    void removeProductFromCartWithQuantityHigherThanCurrentQuantity() {
        CartEntity cart = cartService.createCart();
        cartService.addProduct(cart, productIdOne, 4);
        CartEntity updatedCart = cartService.addProduct(cart, productIdTwo, 4);

        CartEntity finalUpdatedCart =cartService.removeProduct(updatedCart, productIdOne, 6);
        assertThat(finalUpdatedCart.getLineItems()).hasSize(1);
    }

    @Test
    void removeProductFromCartWithNegativeQuantity() {
        CartEntity cart = cartService.createCart();
        CartEntity updatedCart = cartService.addProduct(cart, productIdOne, 4);
        assertThrows(IllegalArgumentException.class, () -> cartService.removeProduct(updatedCart, productIdOne, -1));
    }

    @Test
    void removeProductFromCartWhenProductNotInCart() {
        CartEntity cart = cartService.createCart();
        assertThrows(EntityNotFoundException.class, () -> cartService.removeProduct(cart, 1L, 2));

        CartEntity updatedCart = cartService.addProduct(cart, productIdOne, 4);
        Long differentProductId = productIdOne + 4L;
        assertThrows(EntityNotFoundException.class, () -> cartService.removeProduct(updatedCart, differentProductId, 2));
    }

    @Test
    void clearCart() {
        CartEntity cart = cartService.createCart();
        cartService.addProduct(cart, productIdOne, 4);
        CartEntity updatedCart = cartService.addProduct(cart, productIdTwo, 4);
        CartEntity finalCart = cartService.clearCart(updatedCart);
        assertThat(finalCart.getLineItems()).isEmpty();
        assertThat(cartLineItemRepository.findAll()).isEmpty();
    }

    @Test
    void clearEmptyCart() {
        CartEntity cart = cartService.createCart();
        CartEntity finalCart = cartService.clearCart(cart);
        assertThat(finalCart.getLineItems()).isEmpty();
        assertThat(cartLineItemRepository.findAll()).isEmpty();
    }

    @Test
    void getCart() {
        CartEntity cart = cartService.createCart();
        CartEntity retrievedCart = cartService.getCartById(cart.getId());
        assertThat(retrievedCart).isNotNull();
    }

    @Test
    void getNonExistingCart() {
        assertThrows(EntityNotFoundException.class, () -> cartService.getCartById(1L));
    }

    @Test
    void autoCartRemoval() {
        CartEntity cartOne = cartService.createCart();
        cartService.addProduct(cartOne, productIdOne, 4);

        CartEntity cartTwo = cartService.createCart();
        cartService.addProduct(cartTwo, productIdTwo, 4);
        assertThat(cartRepository.findAll()).hasSize(2);

        // Wait for 30 seconds
        Awaitility.await()
                .pollDelay(30, TimeUnit.SECONDS)
                .atMost(35, TimeUnit.SECONDS) // Timeout should be greater than poll delay
                .until(() -> true);

        cartService.getCartById(cartTwo.getId());

        Awaitility.await()
                .atMost(1, TimeUnit.MINUTES)
                .until(() -> cartRepository.findAll().size() == 1);

        Long cartOneId = cartOne.getId();
        Long cartTwoId = cartTwo.getId();
        assertThrows(EntityNotFoundException.class, () -> cartService.getCartById(cartOneId));
        assertThat(cartService.getCartById(cartTwoId)).isNotNull();

        Awaitility.await()
                .atMost(2, TimeUnit.MINUTES)
                .until(() -> cartRepository.findAll().isEmpty());

        assertThrows(EntityNotFoundException.class, () -> cartService.getCartById(cartOneId));
        assertThrows(EntityNotFoundException.class, () -> cartService.getCartById(cartTwoId));

        assertThat(cartRepository.findAll()).isEmpty();

    }
}