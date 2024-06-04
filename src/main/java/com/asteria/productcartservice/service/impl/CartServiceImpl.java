package com.asteria.productcartservice.service.impl;

import com.asteria.productcartservice.exception.CartNotFoundException;
import com.asteria.productcartservice.exception.ProductNotFoundException;
import com.asteria.productcartservice.repository.CartLineItemRepository;
import com.asteria.productcartservice.repository.CartRepository;
import com.asteria.productcartservice.repository.ProductRepository;
import com.asteria.productcartservice.repository.entity.CartEntity;
import com.asteria.productcartservice.repository.entity.CartLineItemEntity;
import com.asteria.productcartservice.repository.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements com.asteria.productcartservice.service.CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CartLineItemRepository cartLineItemRepository;

    private final long cartExpirationTimeInMin;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartLineItemRepository cartLineItemRepository, @Value("${cart.expiration.time.min}") long cartExpirationTimeInMin) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartLineItemRepository = cartLineItemRepository;
        this.cartExpirationTimeInMin = cartExpirationTimeInMin;
    }

    @Override
    public CartEntity createCart() {
        return cartRepository.save(new CartEntity());
    }

    @Override
    @Transactional
    public CartEntity addProduct(CartEntity cartEntity, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        CartLineItemEntity existingItem = findCartLineItem(cartEntity, productId);

        if (existingItem == null) {
            addNewItemToCart(cartEntity, productEntity, quantity);
        } else {
            updateExistingItemQuantity(existingItem, quantity);
        }
        setModifiedTime(cartEntity);

        return cartRepository.save(cartEntity);
    }

    @Override
    @Transactional
    public CartEntity removeProduct(CartEntity cartEntity, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        CartLineItemEntity existingItem = findCartLineItem(cartEntity, productId);
        if (existingItem == null) {
            throw new ProductNotFoundException("Product not in cart");
        }

        if (existingItem.getQuantity() - quantity > 0) {
            existingItem.setQuantity(existingItem.getQuantity() - quantity);
            cartLineItemRepository.save(existingItem);
        } else {
            cartLineItemRepository.delete(existingItem);
            cartEntity.getLineItems().remove(existingItem);
        }
        setModifiedTime(cartEntity);

        return cartRepository.save(cartEntity);
    }

    @Override
    @Transactional
    public CartEntity clearCart(CartEntity cartEntity) {
        List<CartLineItemEntity> lineItems = cartEntity.getLineItems();
        cartLineItemRepository.deleteAll(lineItems);
        cartEntity.getLineItems().clear();
        setModifiedTime(cartEntity);
        return cartRepository.save(cartEntity);
    }

    @Override
    public CartEntity getCartById(Long cartId) {
        Optional<CartEntity> cart = cartRepository.findById(cartId);
        if (cart.isPresent()) {
            this.setModifiedTime(cart.get());
            cartRepository.save(cart.get());
            return cart.get();
        } else {
            throw new CartNotFoundException("Cart with id " + cartId + " not found");
        }
    }

    @Transactional
    @Scheduled(fixedRate = 2000) // Run every 2 seconds
    public void deleteExpiredCartEntities() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(cartExpirationTimeInMin);
        List<CartEntity> cartsForDeletion = cartRepository.getAllExpired(expirationTime);
        for (CartEntity cartEntity : cartsForDeletion) {
            List<CartLineItemEntity> lineItems = cartEntity.getLineItems();
            cartLineItemRepository.deleteAll(lineItems);
            cartEntity.getLineItems().clear();
            cartRepository.save(cartEntity);
        }
        cartRepository.deleteAll(cartsForDeletion);
    }

    private CartLineItemEntity findCartLineItem(CartEntity cartEntity, Long productId) {
        return cartEntity.getLineItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void addNewItemToCart(CartEntity cartEntity, ProductEntity productEntity, int quantity) {
        CartLineItemEntity newItem = new CartLineItemEntity(cartEntity, productEntity, quantity);
        cartLineItemRepository.save(newItem);
        cartEntity.getLineItems().add(newItem);
    }

    private void updateExistingItemQuantity(CartLineItemEntity existingItem, int quantity) {
        existingItem.setQuantity(existingItem.getQuantity() + quantity);
        cartLineItemRepository.save(existingItem);
    }

    private void setModifiedTime(CartEntity cart) {
        cart.setModifiedAt(LocalDateTime.now());
    }
}
