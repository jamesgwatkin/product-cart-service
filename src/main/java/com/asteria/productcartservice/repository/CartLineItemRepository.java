package com.asteria.productcartservice.repository;

import com.asteria.productcartservice.repository.entity.CartLineItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartLineItemRepository extends JpaRepository<CartLineItemEntity, Long> {
}
