package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.responses.AuthenticationResponse;
import com.haratres.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.save(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }


    @PostMapping("/createAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> saveAllProducts(@RequestBody List<ProductDto> productDtoList) {
        return ResponseEntity.ok(productService.saveAll(productDtoList));
    }


    @DeleteMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto updatedProductDto) {
        return ResponseEntity.ok(productService.updateProductByName(updatedProductDto));
    }
}

