package com.asteria.productcartservice.repository;

import com.asteria.productcartservice.repository.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
