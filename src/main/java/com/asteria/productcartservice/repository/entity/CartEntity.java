package com.asteria.productcartservice.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Entity(name = "carts")
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartLineItemEntity> lineItems = new ArrayList<>();

    @Setter
    private LocalDateTime modifiedAt;

    public CartEntity() {
        super();
        this.modifiedAt = LocalDateTime.now();
    }
}
