package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByCode(String code);
    List<Product> findAllByCodeIn(List<String> codes);

    @Query("SELECT p FROM Product p WHERE LOWER(p.code) = LOWER(:code) OR LOWER(p.name) = LOWER(:name)")
    List<Product> findByCodeIgnoreCaseOrNameIgnoreCase(@Param("code") String code, @Param("name") String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%')) OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(@Param("code") String code, @Param("name") String name);


}
