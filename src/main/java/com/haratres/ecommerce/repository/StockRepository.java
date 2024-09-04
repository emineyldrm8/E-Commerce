package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {
    Optional<Stock> findByProductId(Long stockId);
}
