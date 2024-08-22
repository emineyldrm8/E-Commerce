package com.haratres.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id",unique=true,nullable = false)
    private User user;

    @OneToMany(mappedBy="cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartEntry> cartEntries;//1den ne olunca dbde olmuyor ztaen diğerinde var.
    //kartın içindeki ürümü cartentryden sil
}
