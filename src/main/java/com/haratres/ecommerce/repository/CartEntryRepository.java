package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.CartEntry;
import com.haratres.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartEntryRepository extends JpaRepository<CartEntry,Long> {
    Optional<CartEntry> findByCartAndProduct(Cart cart, Product product);
}
