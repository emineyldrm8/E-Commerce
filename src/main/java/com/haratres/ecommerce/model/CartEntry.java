package com.haratres.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_entry")
@Getter
@Setter
public class CartEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="cart_id", nullable = false)
    @JsonIgnore
    private Cart cart; // Her CartEntry bir Cart'a ait olmalı

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product; // Her CartEntry bir Product'a ait olmalı

    @Column(name="quantity")
    private int quantity;
}
