package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.*;
import com.haratres.ecommerce.exception.*;
import com.haratres.ecommerce.mapper.StockMapper;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Stock;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final StockMapper stockMapper = StockMapper.INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, StockService stockService) {
        this.productRepository = productRepository;
        this.stockService = stockService;
    }

    public List<ProductDto> getAllProducts() {
        return productMapper.toProductDtoList(productRepository.findAll());
    }

    public ProductDto save(CreateProductDto createProductDto) {
        Product product = productMapper.toProductFromCreateProductDto(createProductDto);

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
                    .map(productMapper::toProductFromCreateProductDto)
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
            Product updatedProduct = productMapper.toProductFromUpdateProductDto(updatedProductDto);
            updatedProduct.setId(id);

            Product savedProduct = productRepository.save(updatedProduct);
            logger.info("Updated product with code: {}", updatedProduct.getCode());
            return productMapper.toProductDto(savedProduct);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating product with id: {}", id, e);
            throw new NotUpdatedException("Failed to update the product with id: " + id, e);
        }
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

    public StockDto createStockForProduct(Long productId, CreateStockDto stockDto) {
        try {
            Product product = getProductById(productId);
            Stock existingStock = product.getStock();
            if (Objects.equals(existingStock.getQuantity(), 0)) {
                existingStock.setQuantity(stockDto.getQuantity());
            } else {
                throw new NotSavedException("This product already has stock. You cannot create another one.");
            }
            Stock updatedStock = stockService.saveStock(existingStock);
            return stockMapper.toStockDto(updatedStock);
        } catch (Exception e) {
            throw new NotSavedException("An error occurred while processing the stock for product ID: " + productId, e);
        }
    }
    public StockDto updateStock(Long productId, UpdateStockDto stock) {
        Product product = getProductById(productId);
        if (Objects.isNull(product.getStock())) {
            throw new NotFoundException("Product does not have a stock to update.");
        }
        Stock updatedStock = stockMapper.toStockFromUpdateStockDto(stock);
        updatedStock.setId(product.getStock().getId());
        updatedStock.setProduct(product);;
        updatedStock = stockService.saveStock(updatedStock);
        product.setStock(updatedStock);
        productRepository.save(product);
        return stockMapper.toStockDto(updatedStock);
    }
    public void deleteStock(Long productId) {
        try {
            Product product = getProductById(productId);
            stockService.deleteStock(product.getStock());
        } catch (Exception e) {
            logger.error("Failed to delete stock with id: {}", productId);
            throw new NotDeletedException("Failed to delete stock with id: " + productId, e);
        }
    }

}
