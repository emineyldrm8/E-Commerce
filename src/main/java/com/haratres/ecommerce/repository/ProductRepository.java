package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);
    boolean existsByName(String name);
    List<Product> findAllByNameIn(List<String> names);
}
