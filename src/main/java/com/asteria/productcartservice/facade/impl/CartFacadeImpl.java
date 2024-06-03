package com.asteria.productcartservice.facade.impl;

import com.asteria.productcartservice.cart.model.*;
import com.asteria.productcartservice.facade.CartFacade;
import com.asteria.productcartservice.repository.entity.CartEntity;
import com.asteria.productcartservice.repository.entity.CartLineItemEntity;
import com.asteria.productcartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartFacadeImpl implements CartFacade {

    private final CartService cartService;

    @Autowired
    public CartFacadeImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public CartId createCart() {
        CartEntity cart = cartService.createCart();
        CartId cartId = new CartId();
        cartId.setId(cart.getId());
        return cartId;
    }

    @Override
    public Cart getCart(Long cartId) {
        CartEntity cart = cartService.getCartById(cartId);
        return transformCart(cart);
    }

    @Override
    public List<Product> getCartProducts(Long cartId) {
        CartEntity cart = cartService.getCartById(cartId);
        return transformProducts(cart);
    }

    @Override
    public void addProductToCart(Long cartId, Long productId, Integer quantity) {
        CartEntity cart = cartService.getCartById(cartId);
        cartService.addProduct(cart, productId, quantity);
    }

    @Override
    public void removeProductFromCart(Long cartId, Long productId, Integer quantity) {
        CartEntity cart = cartService.getCartById(cartId);
        cartService.removeProduct(cart, productId, quantity);
    }

    @Override
    public void clearCart(Long cartId) {
        CartEntity cart = cartService.getCartById(cartId);
        cartService.clearCart(cart);
    }
    
    private Cart transformCart(CartEntity cart) {
        Cart transformedCart = new Cart();
        List<CartLineItemsInner> responseLineItems = new ArrayList<>();
        for(CartLineItemEntity lineItem:cart.getLineItems()) {
            CartLineItemsInner responseCartLineItems = new CartLineItemsInner();
            responseCartLineItems.setQuantity(lineItem.getQuantity());
            CartLineItemsInnerProduct responseProduct = new CartLineItemsInnerProduct();
            responseProduct.setName(lineItem.getProduct().getName());
            responseProduct.setPrice(lineItem.getProduct().getPrice());
            responseProduct.description(lineItem.getProduct().getDescription());
            responseCartLineItems.product(responseProduct);
            responseLineItems.add(responseCartLineItems);
        }
        transformedCart.setLineItems(responseLineItems);
        return transformedCart;
    }

    private List<Product> transformProducts(CartEntity cart)
    {
        List<Product> responseProducts = new ArrayList<>();
        for(CartLineItemEntity lineItem:cart.getLineItems()) {
            Product respomseProduct = new Product();
            respomseProduct.setName(lineItem.getProduct().getName());
            respomseProduct.setPrice(lineItem.getProduct().getPrice());
            respomseProduct.description(lineItem.getProduct().getDescription());
            responseProducts.add(respomseProduct);
        }
        return responseProducts;
    }
}
