package com.asteria.productcartservice.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "cart")
@EqualsAndHashCode
@Entity(name = "cart_line_items")
@Table(name = "cart_line_items")
public class CartLineItemEntity {

    public CartLineItemEntity() {

    }

    @Id
    @GeneratedValue
    @Column(name = "cart_line_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    private Integer quantity;

    public CartLineItemEntity(CartEntity cart, ProductEntity product, Integer quantity) {
        this.product = product;
        this.cart = cart;
        this.quantity = quantity;
    }
}
