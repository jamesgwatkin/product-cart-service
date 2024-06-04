package com.asteria.productcartservice.service;

import com.asteria.productcartservice.repository.entity.CartEntity;
import org.springframework.transaction.annotation.Transactional;

public interface CartService {
    CartEntity createCart();

    @Transactional
    CartEntity addProduct(CartEntity cartEntity, Long productId, int quantity);

    @Transactional
    CartEntity removeProduct(CartEntity cartEntity, Long productId, int quantity);

    @Transactional
    CartEntity clearCart(CartEntity cartEntity);

    CartEntity getCartById(Long cartId);
}
