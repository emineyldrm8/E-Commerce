package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.responses.AuthenticationResponse;
import com.haratres.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.save(productDto));
    }

    @PostMapping("/createAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> saveAllProducts(@RequestBody List<ProductDto> productDtoList) {
        return ResponseEntity.ok(productService.saveAll(productDtoList));
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto updatedProductDto) {
        return ResponseEntity.ok(productService.updateProductByName(updatedProductDto));
    }
}

