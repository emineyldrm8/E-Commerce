package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.CreatePriceDto;
import com.haratres.ecommerce.dto.PriceDto;
import com.haratres.ecommerce.dto.UpdatePriceDto;
import com.haratres.ecommerce.model.Price;
import com.haratres.ecommerce.service.PriceService;
import com.haratres.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/prices")
public class ProductPriceController {
    private final PriceService priceService;
    private final ProductService productService;


    public ProductPriceController(PriceService priceService, ProductService productService) {
        this.priceService = priceService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<PriceDto> getPrice(@PathVariable Long productId) {
        PriceDto price = priceService.getPriceByProductId(productId);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PriceDto>> getPrices(@PathVariable Long productId) {
        return ResponseEntity.ok(priceService.getPrices());
    }

    @PostMapping
    public ResponseEntity<PriceDto> createPrice(@PathVariable Long productId, @RequestBody CreatePriceDto price) {
        PriceDto savePrice = productService.createPriceForProduct(productId, price);
        return new ResponseEntity<>(savePrice, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PriceDto> updatePrice(@PathVariable Long productId, @RequestBody UpdatePriceDto price) {
        PriceDto updatedPrice = productService.updatePrice(productId, price);
        return ResponseEntity.ok(updatedPrice);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePrice(@PathVariable Long productId) {
        productService.deletePrice(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllPrices() {
        productService.deletePrices();
        return ResponseEntity.noContent().build();
    }
}
