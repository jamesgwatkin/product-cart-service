package com.asteria.productcartservice.controller;

import com.asteria.productcartservice.facade.ProductFacade;
import com.asteria.productcartservice.product.api.ProductApi;
import com.asteria.productcartservice.product.model.Product;
import com.asteria.productcartservice.product.model.ProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {

    private final ProductFacade productFacade;

    @Autowired
    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Override
    public ResponseEntity<ProductId> createProduct(Product product){
        ProductId productId = productFacade.createProduct(product.getName(), product.getDescription(), product.getPrice());
        return ResponseEntity.ok(productId);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        productFacade.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Product> getProduct(Long productId) {
        return ResponseEntity.ok(productFacade.getProduct(productId));
    }

    @Override
    public ResponseEntity<Void> updateProduct(Long productId, Product product){
        productFacade.updateProduct(productId, product.getName(), product.getDescription(), product.getPrice());
        return ResponseEntity.ok().build();
    }
}
