package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Product savedProduct = productRepository.save(product);
        logger.info("Saved product with id: {}", savedProduct.getId());
        return productMapper.toProductDto(savedProduct);
    }

    public List<ProductDto> saveAll(List<ProductDto> productDtoList) {
        logger.info("Saving {} products.", productDtoList.size());
        List<Product> products = productMapper.toProductList(productDtoList);
        List<Product> savedProducts = productRepository.saveAll(products);
        logger.info("Saved {} products.", savedProducts.size());
        return productMapper.toProductDtoList(savedProducts);
    }

    public void deleteProductById(Long id) {
        logger.info("Deleting product with id: {}", id);
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            logger.info("Deleted product with id: {}", id);
        } else {
            logger.warn("Product with id {} not found for deletion.", id);
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
        product.setName(updatedProduct.getName());
        product.setTitle(updatedProduct.getTitle());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setColor(updatedProduct.getColor());
        product.setSize(updatedProduct.getSize());
        Product savedProduct = productRepository.save(product);
        logger.info("Updated product with name: {}", updatedProduct.getName());
        return productMapper.toProductDto(savedProduct);
    }
}
