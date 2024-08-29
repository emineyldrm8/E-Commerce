package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.CreateStockDto;
import com.haratres.ecommerce.dto.StockDto;
import com.haratres.ecommerce.dto.UpdateStockDto;
import com.haratres.ecommerce.service.StockService;
import com.haratres.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{productId}/stocks")
public class ProductStockController {
    private final StockService stockService;
    private final ProductService productService;

    public ProductStockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<StockDto> getStock(@PathVariable Long productId) {
        StockDto stock = stockService.getStockByProductId(productId);
        return ResponseEntity.ok(stock);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StockDto> createStock(@PathVariable Long productId, @RequestBody CreateStockDto stock) {
        StockDto saveStock = productService.createStockForProduct(productId, stock);
        return new ResponseEntity<>(saveStock, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StockDto> updateStock(@PathVariable Long productId, @RequestBody UpdateStockDto stock) {
        StockDto updatedStock = productService.updateStock(productId, stock);
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteStock(@PathVariable Long productId) {
        productService.deleteStock(productId);
        return ResponseEntity.noContent().build();
    }
}
