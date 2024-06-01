package com.asteria.productcartservice.facade.impl;

import com.asteria.productcartservice.facade.ProductFacade;
import com.asteria.productcartservice.product.model.Product;
import com.asteria.productcartservice.product.model.ProductId;
import com.asteria.productcartservice.repository.entity.ProductEntity;
import com.asteria.productcartservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;

    @Autowired
    public ProductFacadeImpl(ProductService productService) {
        super();
        this.productService = productService;
    }

    @Override
    public ProductId createProduct(String name, String description, BigDecimal price) {
        ProductEntity product = productService.createProduct(name, description, price);
        ProductId productId = new ProductId();
        productId.setId(product.getId());
        return productId;
    }

    @Override
    public void deleteProduct(Long productId) {
        productService.deleteProduct(productId);
    }

    @Override
    public Product getProduct(Long productId) {
        return transformProductEntity(productService.getProductById(productId));
    }

    @Override
    public void updateProduct(Long productId, String name, String description, BigDecimal price) {
        ProductEntity productEntity = productService.getProductById(productId);
        productEntity.setName(name);
        productEntity.setDescription(description);
        productEntity.setPrice(price);
        productService.updateProduct(productEntity);
    }

    private Product transformProductEntity(ProductEntity productEntity) {
        Product product = new Product();
        product.setName(productEntity.getName());
        product.setDescription(productEntity.getDescription());
        product.setPrice(productEntity.getPrice());
        return product;
    }
}
