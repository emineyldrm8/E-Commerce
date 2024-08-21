package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.exception.*;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        logger.info("Fetching all products.");
        List<Product> products = productRepository.findAll();
        logger.info("Found {} products.", products.size());
        return products;
    }

    public ProductDto save(ProductDto productDto) {
        logger.info("Saving product: {}", productDto);
        Product product = productMapper.toProduct(productDto);
        if (productRepository.existsByName(product.getName())) {
            logger.error("Conflict occurred: Product with name {} already exists.", productDto.getName());
            throw new DuplicateEntryException("Product with name " + productDto.getName() + " already exists.");
        }
        try {
            Product savedProduct = productRepository.save(product);
            logger.info("Saved product with id: {}", savedProduct.getId());
            return productMapper.toProductDto(savedProduct);
        } catch (Exception e) {
            logger.error("Failed to save the product: {}", productDto.getName());
            throw new NotSavedException("Failed to save the product: " + productDto.getName(), e);
        }
    }

    public List<ProductDto> saveAll(List<ProductDto> productDtoList) {
        logger.info("Saving {} products.", productDtoList.size());
        List<Product> products = productMapper.toProductList(productDtoList);
        List<String> productNames = products.stream().map(Product::getName).collect(Collectors.toList());
        List<Product> existingProducts = productRepository.findAllByNameIn(productNames);
        if (!existingProducts.isEmpty()) {
            List<String> existingProductNames = existingProducts.stream().map(Product::getName).collect(Collectors.toList());
            logger.error("Conflict occurred: Products with names {} already exist.", existingProductNames);
            throw new DuplicateEntryException("Products with names " + existingProductNames + " already exist.");
        }
        try {
            List<Product> savedProducts = productRepository.saveAll(products);
            logger.info("Saved {} products.", savedProducts.size());
            return productMapper.toProductDtoList(savedProducts);
        } catch (Exception e) {
            logger.error("Failed to save the product list: {}", productDtoList);
            throw new NotSavedException("Failed to save the product list : " + productDtoList.stream().map(productMapper::toProduct).collect(Collectors.toList()), e);
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
            logger.error("Product with id {} not found for deletion.", id);
            throw new NotDeletedException("Failed to delete the product with id: " + id, e);
        }
    }

    public Product getProductById(Long id) {
        logger.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", id);
                    return new NotFoundException("Product not found with id:" + id);
                });
        logger.info("Found product with id: {}", id);
        return product;
    }

    public ProductDto updateProductByName(ProductDto updatedProductDto) {

        logger.info("Updating product with name: {}", updatedProductDto.getName());
        Product updatedProduct = productMapper.toProduct(updatedProductDto);
        Product product = productRepository.findByName(updatedProduct.getName())
                .orElseThrow(() -> {
                    logger.error("Product not found with name: {}", updatedProduct.getName());
                    return new NotFoundException("Product not found with name: " + updatedProduct.getName());
                });
        try {
            product.setName(updatedProduct.getName());
            product.setTitle(updatedProduct.getTitle());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setColor(updatedProduct.getColor());
            product.setSize(updatedProduct.getSize());
            Product savedProduct = productRepository.save(product);
            logger.info("Updated product with name: {}", updatedProduct.getName());
            return productMapper.toProductDto(savedProduct);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating product with name: {}", updatedProductDto.getName(), e);
            throw new NotUpdatedException("Failed to update the product with name: " + updatedProductDto.getName(), e);
        }
    }

    public boolean productNameExists(String name) {
        return productRepository.existsByName(name);
    }
}
