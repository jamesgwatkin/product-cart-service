package com.asteria.productcartservice.facade.impl;

import com.asteria.productcartservice.cart.model.*;
import com.asteria.productcartservice.repository.entity.CartEntity;
import com.asteria.productcartservice.repository.entity.CartLineItemEntity;
import com.asteria.productcartservice.repository.entity.ProductEntity;
import com.asteria.productcartservice.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartFacadeImplTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartFacadeImpl cartFacade;

    @Mock
    private CartEntity cartEntity;
    @Mock
    private ProductEntity productEntityOne;
    @Mock
    private ProductEntity productEntityTwo;
    @Mock
    private CartLineItemEntity cartLineItemEntityOne;
    @Mock
    private CartLineItemEntity cartLineItemEntityTwo;

    @Test
    void createCart() {
        when(cartEntity.getId()).thenReturn(1L);
        when(cartService.createCart()).thenReturn(cartEntity);

        CartId result = cartFacade.createCart();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartService, times(1)).createCart();
    }

    @Test
    void getCart() {
        when(productEntityOne.getPrice()).thenReturn(BigDecimal.valueOf(10.00));
        when(productEntityOne.getName()).thenReturn("Test Product");
        when(productEntityOne.getDescription()).thenReturn("Test Description");
        when(cartLineItemEntityOne.getProduct()).thenReturn(productEntityOne);
        when(cartLineItemEntityOne.getQuantity()).thenReturn(2);
        when(cartEntity.getLineItems()).thenReturn(Collections.singletonList(cartLineItemEntityOne));
        when(cartService.getCartById(1L)).thenReturn(cartEntity);

        Cart result = cartFacade.getCart(1L);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(20.00).setScale(2, RoundingMode.CEILING), result.getTotal());
        assertEquals(1, result.getLineItems().size());
        CartLineItemsInner lineItem = result.getLineItems().getFirst();
        assertEquals(2, lineItem.getQuantity());
        CartLineItemsInnerProduct product = lineItem.getProduct();
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(BigDecimal.valueOf(10.00), product.getPrice());
        verify(cartService, times(1)).getCartById(1L);
    }

    @Test
    void getCartProducts() {
        when(productEntityOne.getPrice()).thenReturn(BigDecimal.valueOf(10.00));
        when(productEntityOne.getName()).thenReturn("Test Product");
        when(productEntityOne.getDescription()).thenReturn("Test Description");
        when(cartLineItemEntityOne.getProduct()).thenReturn(productEntityOne);

        when(productEntityTwo.getPrice()).thenReturn(BigDecimal.valueOf(19.00));
        when(productEntityTwo.getName()).thenReturn("Another Test Product");
        when(productEntityTwo.getDescription()).thenReturn("Another Test Description");
        when(cartLineItemEntityTwo.getProduct()).thenReturn(productEntityTwo);

        List<CartLineItemEntity> lineItems = new ArrayList<>();
        lineItems.add(cartLineItemEntityOne);
        lineItems.add(cartLineItemEntityTwo);

        when(cartEntity.getLineItems()).thenReturn(lineItems);
        when(cartService.getCartById(1L)).thenReturn(cartEntity);

        List<Product> result = cartFacade.getCartProducts(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        Product productOne = result.getFirst();
        assertEquals("Test Product", productOne.getName());
        assertEquals("Test Description", productOne.getDescription());
        assertEquals(BigDecimal.valueOf(10.00), productOne.getPrice());
        Product productTwo = result.getLast();
        assertEquals("Another Test Product", productTwo.getName());
        assertEquals("Another Test Description", productTwo.getDescription());
        assertEquals(BigDecimal.valueOf(19.00), productTwo.getPrice());


        verify(cartService, times(1)).getCartById(1L);
    }

    @Test
    void addProductToCart() {
        when(cartService.getCartById(1L)).thenReturn(cartEntity);

        cartFacade.addProductToCart(1L, 1L, 2);

        verify(cartService, times(1)).getCartById(1L);
        verify(cartService, times(1)).addProduct(cartEntity, 1L, 2);
    }

    @Test
    void removeProductFromCart() {
        when(cartService.getCartById(1L)).thenReturn(cartEntity);

        cartFacade.removeProductFromCart(1L, 1L, 2);

        verify(cartService, times(1)).getCartById(1L);
        verify(cartService, times(1)).removeProduct(cartEntity, 1L, 2);
    }

    @Test
    void clearCart() {
        when(cartService.getCartById(1L)).thenReturn(cartEntity);

        cartFacade.clearCart(1L);

        verify(cartService, times(1)).getCartById(1L);
        verify(cartService, times(1)).clearCart(cartEntity);
    }
}