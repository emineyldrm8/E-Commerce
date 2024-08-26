package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByCode(String code);
    List<Product> findAllByCodeIn(List<String> codes);
}
