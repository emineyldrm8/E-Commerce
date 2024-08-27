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

@RestController
@RequestMapping("/api/products/{productId}/prices")
public class ProductPriceController {
    private final PriceService priceService;
    private final ProductService productService;


    public ProductPriceController(PriceService priceService, ProductService productService) {
        this.priceService = priceService;
        this.productService = productService;
    }

    @GetMapping //ok
    public ResponseEntity<PriceDto> getPrice(@PathVariable Long productId) {
        PriceDto price = priceService.getPriceByProductId(productId);
        return ResponseEntity.ok(price);
    }

    @PostMapping //bu biiti
    public ResponseEntity<PriceDto> createPrice(@PathVariable Long productId, @RequestBody CreatePriceDto price) {
        PriceDto savePrice = productService.createPriceForProduct(productId,price);
        return new ResponseEntity<>(savePrice, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PriceDto> updatePrice(@PathVariable Long productId, @RequestBody UpdatePriceDto price) {
        PriceDto updatedPrice=productService.updatePrice(productId,price);
        return ResponseEntity.ok(updatedPrice);
    }

   /* @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        addressService.deleteAddressByAddressId(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAddresses(@PathVariable Long userId) {
        addressService.deleteAddresses(userId);
        return ResponseEntity.noContent().build();
    }*/
}
