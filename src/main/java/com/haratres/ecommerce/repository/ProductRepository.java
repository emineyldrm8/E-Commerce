package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByCode(String code);
    List<Product> findAllByCodeIn(List<String> codes);
    List<Product> findByCodeIgnoreCaseOrNameIgnoreCase(String code, String name);Q
    List<Product> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name);

}
