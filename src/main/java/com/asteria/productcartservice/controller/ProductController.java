package com.asteria.productcartservice.controller;

import com.asteria.productcartservice.product.api.ProductApi;
import com.asteria.productcartservice.product.model.Product;
import com.asteria.productcartservice.product.model.ProductId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {

    @Override
    public ResponseEntity<ProductId> createProduct(Product product){
        return ResponseEntity.ok(new ProductId());
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Integer productId) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Product> getProduct(Integer productId) {
        return ResponseEntity.ok(new Product());
    }

    @Override
    public ResponseEntity<Void> updateProduct(Integer productId, Product product){
        return ResponseEntity.ok().build();
    }
}
