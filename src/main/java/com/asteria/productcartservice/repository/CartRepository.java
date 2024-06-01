package com.asteria.productcartservice.repository;

import com.asteria.productcartservice.repository.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("SELECT c FROM carts c WHERE c.modifiedAt < :expirationTime")
    List<CartEntity> getAllExpired(@Param("expirationTime") LocalDateTime expirationTime);
}
