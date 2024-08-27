package com.haratres.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code"})
})
@Getter
@Setter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @Column(name="price",nullable = false)
    private Price price;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "size", nullable = false)
    private String size;
}
