package com.asteria.productcartservice.controller;

import com.asteria.productcartservice.cart.api.CartApi;
import com.asteria.productcartservice.cart.model.Cart;
import com.asteria.productcartservice.cart.model.CartId;
import com.asteria.productcartservice.cart.model.Product;
import com.asteria.productcartservice.facade.CartFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController implements CartApi {

    private final CartFacade cartFacade;

    @Autowired
    public CartController(CartFacade cartFacade) {
        this.cartFacade = cartFacade;
    }

    @Override
    public ResponseEntity<CartId> createCart() {
        return ResponseEntity.ok(cartFacade.createCart());
    }

    @Override
    public ResponseEntity<Cart> getCart(Long cartId) {
        return ResponseEntity.ok(cartFacade.getCart(cartId));
    }

    @Override
    public ResponseEntity<List<Product>> getCartProducts(Long cartId) {
        return ResponseEntity.ok(cartFacade.getCartProducts(cartId));
    }

    @Override
    public ResponseEntity<Void> addProductToCart(Long cartId, Long productId, Integer quantity) {
        cartFacade.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeProductFromCart(Long cartId, Long productId, Integer quantity) {
        cartFacade.removeProductFromCart(cartId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> clearCart(Long cartId) {
        cartFacade.clearCart(cartId);
        return ResponseEntity.ok().build();
    }

}
