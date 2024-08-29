package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.CreateProductDto;
import com.haratres.ecommerce.dto.PageRequestDto;
import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UpdateProductDto;
import com.haratres.ecommerce.exception.*;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PaginationService paginationService;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    public Page<ProductDto> getAllProducts(PageRequestDto dto) {
        List<String> validColumns = paginationService.getValidSortColumns(Product.class);
        if (!validColumns.contains(dto.getSortByColumn())) {
            logger.error("Invalid sort column: {}" ,dto.getSortByColumn());
            throw new NotFoundException("Invalid sort column: " + dto.getSortByColumn());
        }
        Pageable pageable = paginationService.getPageable(dto);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toProductDto);
    }

    public ProductDto save(CreateProductDto createProductDto) {
        Product product = productMapper.toProduct(createProductDto);

        if (productRepository.existsByCode(product.getCode())) {
            logger.error("Conflict occurred: Product with code {} already exists.", createProductDto.getCode());
            throw new DuplicateEntryException("Product with code " + createProductDto.getCode() + " already exists.");
        }

        try {
            Product savedProduct = productRepository.save(product);
            logger.info("Saved product with id: {}", savedProduct.getId());
            return productMapper.toProductDto(savedProduct);
        } catch (Exception e) {
            logger.error("Failed to save the product: {}", createProductDto.getCode());
            throw new NotSavedException("Failed to save the product: " + createProductDto.getCode(), e);
        }
    }

    public List<ProductDto> saveAll(List<CreateProductDto> createProductDtoList) {
        List<Product> products = productMapper.toProductListFromCreate(createProductDtoList);
        List<String> productCodes = products.stream().map(Product::getCode).collect(Collectors.toList());
        List<Product> existingProducts = productRepository.findAllByCodeIn(productCodes);

        if (!existingProducts.isEmpty()) {
            List<String> existingProductCodes = existingProducts.stream().map(Product::getCode).collect(Collectors.toList());
            logger.error("Conflict occurred: Products with codes {} already exist.", existingProductCodes);
            throw new DuplicateEntryException("Products with codes " + existingProductCodes + " already exist.");
        }

        try {
            List<Product> savedProducts = productRepository.saveAll(products);
            logger.info("Saved {} products.", savedProducts.size());
            return productMapper.toProductDtoList(savedProducts);
        } catch (Exception e) {
            logger.error("Failed to save the product list: {}", createProductDtoList);
            throw new NotSavedException("Failed to save the product list: " + createProductDtoList.stream()
                    .map(productMapper::toProduct)
                    .collect(Collectors.toList()), e);
        }
    }

    public void deleteProductById(Long id) {
        logger.info("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            logger.error("Product with id {} not found for deletion.", id);
            throw new NotFoundException("Product with id " + id + " not found for deletion.");
        }

        try {
            productRepository.deleteById(id);
            logger.info("Deleted product with id: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete the product with id: {}", id, e);
            throw new NotDeletedException("Failed to delete the product with id: " + id, e);
        }
    }

    public ProductDto getProductDtoById(Long id) {
        Product product = getProductById(id);
        return productMapper.toProductDto(product);
    }

    public ProductDto updateProductById(Long id, UpdateProductDto updatedProductDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", id);
                    return new NotFoundException("Product not found with id: " + id);
                });

        try {
            Product updatedProduct = productMapper.toProduct(updatedProductDto);
            updatedProduct.setId(id);

            Product savedProduct = productRepository.save(updatedProduct);
            logger.info("Updated product with code: {}", updatedProduct.getCode());
            return productMapper.toProductDto(savedProduct);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating product with id: {}", id, e);
            throw new NotUpdatedException("Failed to update the product with id: " + id, e);
        }
    }

    public  Page<ProductDto> searchProducts(PageRequestDto dto,String text) {
        Pageable pageable = paginationService.getPageable(dto);
        String cleanedText = text.trim().toLowerCase();
        List<String> keywords = Arrays.asList(cleanedText.split("\\s+"));
        List<Product> products = productRepository.findByCodeIgnoreCaseOrNameIgnoreCase(cleanedText, cleanedText);
        for (String keyword : keywords) {
            products.addAll(productRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword));
        }
        List<Product> uniqueProducts = products.stream().distinct().collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), uniqueProducts.size());
        List<Product> pagedProducts = uniqueProducts.subList(start, end);
        Page<Product> productPage = new PageImpl<>(pagedProducts, pageable, uniqueProducts.size());
        return productPage.map(productMapper::toProductDto);
    }


    public boolean productCodeExists(String code) {
        return productRepository.existsByCode(code);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", id);
                    return new NotFoundException("Product not found with id: " + id);
                });
    }
}