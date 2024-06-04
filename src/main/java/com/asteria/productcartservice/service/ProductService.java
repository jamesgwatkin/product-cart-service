package com.asteria.productcartservice.service;

import com.asteria.productcartservice.repository.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductEntity createProduct(String name, String description, BigDecimal price);

    ProductEntity updateProduct(ProductEntity product);

    List<ProductEntity> getAllProducts();

    ProductEntity getProductById(Long id);

    void deleteProduct(Long id);
}
