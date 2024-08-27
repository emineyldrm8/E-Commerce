package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.CreateProductDto;
import com.haratres.ecommerce.dto.PageRequestDto;
import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UpdateProductDto;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(name = "page") int pageNumber,
            @RequestParam(name = "size") int pageSize,
            @RequestParam(name = "sort") Sort.Direction sort,
            @RequestParam(name = "sortBy") String sortByColumn){
        PageRequestDto dto = new PageRequestDto(pageNumber,pageSize,sort,sortByColumn);
        Page<ProductDto> productPage = productService.getAllProducts(dto);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDtoById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam(name = "query")  String text) {
        return ResponseEntity.ok(productService.searchProducts(text));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody CreateProductDto createProductDto) {
        ProductDto savedProduct = productService.save(createProductDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/createAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ProductDto>> saveAllProducts(@RequestBody List<CreateProductDto> productDtoList) {
        return ResponseEntity.ok(productService.saveAll(productDtoList));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> updateProductById(
            @PathVariable Long id,
            @RequestBody UpdateProductDto updatedProductDto) {
        return ResponseEntity.ok(productService.updateProductById(id, updatedProductDto));
    }
}
