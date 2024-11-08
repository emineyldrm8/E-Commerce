package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
  Optional<Cart> findByUserId(Long userId);
}
