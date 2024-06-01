package com.asteria.productcartservice.facade;

import com.asteria.productcartservice.product.model.Product;
import com.asteria.productcartservice.product.model.ProductId;

import java.math.BigDecimal;

public interface ProductFacade {

    ProductId createProduct(String name, String description, BigDecimal price);

    void deleteProduct(Long productId);

    Product getProduct(Long productId);

    void updateProduct(Long productId, String name, String description, BigDecimal price);
}
