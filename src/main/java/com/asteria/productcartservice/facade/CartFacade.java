package com.asteria.productcartservice.facade;

import com.asteria.productcartservice.cart.model.Cart;
import com.asteria.productcartservice.cart.model.CartId;
import com.asteria.productcartservice.cart.model.Product;

import java.util.List;

public interface CartFacade {

    CartId createCart();

    Cart getCart(Long cartId);

    List<Product> getCartProducts(Long cartId);

    void addProductToCart(Long cartId, Long productId, Integer quantity);

    void removeProductFromCart(Long cartId, Long productId, Integer quantity);

    void clearCart(Long cartId);
}
