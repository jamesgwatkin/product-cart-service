package com.asteria.productcartservice.service;

import com.asteria.productcartservice.repository.entity.ProductEntity;
import com.asteria.productcartservice.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity createProduct(String name, String description, BigDecimal price) {
        ProductEntity product = new ProductEntity(name, description, price);
        return productRepository.save(product);
    }

    public ProductEntity updateProduct(ProductEntity product) {
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID must be set for update operation");
        }
        return productRepository.save(product);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product with ID " + id + " does not exist");
        }
        productRepository.deleteById(id);
    }
}
