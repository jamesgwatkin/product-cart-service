package com.asteria.productcartservice.service.impl;

import com.asteria.productcartservice.exception.ProductNotFoundException;
import com.asteria.productcartservice.repository.ProductRepository;
import com.asteria.productcartservice.repository.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements com.asteria.productcartservice.service.ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity createProduct(String name, String description, BigDecimal price) {
        ProductEntity product = new ProductEntity(name, description, price);
        return productRepository.save(product);
    }

    @Override
    public ProductEntity updateProduct(ProductEntity product) {
        if (product.getId() == null) {
            throw new ProductNotFoundException("Product ID must be set for update operation");
        }
        return productRepository.save(product);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " does not exist");
        }
        productRepository.deleteById(id);
    }
}
