package com.haratres.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Stock")
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @OneToOne(mappedBy="stock")
    @JsonIgnore
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}