package com.asteria.productcartservice.controller;

import com.asteria.productcartservice.cart.api.CartApi;
import com.asteria.productcartservice.cart.model.Cart;
import com.asteria.productcartservice.cart.model.CartId;
import com.asteria.productcartservice.cart.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CartController implements CartApi {

    @Override
    public ResponseEntity<CartId> createCart() {
        return ResponseEntity.ok(new CartId());
    }

    @Override
    public ResponseEntity<Cart> getCart(Integer cartId) {
        return ResponseEntity.ok(new Cart());
    }

    @Override
    public ResponseEntity<List<Product>> getCartProducts(Integer cartId) {
        List<Product> products = new ArrayList<>();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<Void> addProductToCart(Integer cartId, Integer productId, Integer quantity) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeProductFromCart(Integer cartId, Integer productId, Integer quantity) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> clearCart(Integer cartId) {
        return ResponseEntity.ok().build();
    }

}
