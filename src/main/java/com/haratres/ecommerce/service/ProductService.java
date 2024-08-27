package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.*;
import com.haratres.ecommerce.exception.*;
import com.haratres.ecommerce.mapper.PriceMapper;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.model.Price;
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
    private final PriceService priceService;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final PriceMapper priceMapper = PriceMapper.INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    public ProductService(ProductRepository productRepository, PriceService priceService) {
        this.productRepository = productRepository;
        this.priceService = priceService;
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

    public PriceDto createPriceForProduct(Long productId, CreatePriceDto priceDto) {
        Product product = getProductById(productId);
        if (Objects.nonNull(product.getPrice())) {
            throw new DuplicateEntryException("Product already has a price.");
        }
        Price price=priceMapper.toPriceFromCreatePriceDto(priceDto);
        price.setProduct(product);
        return priceMapper.toPriceDto(priceService.savePrice(price));
    }

    public PriceDto updatePrice(Long productId, UpdatePriceDto price) {
        Product product = getProductById(productId);
        if (Objects.isNull(product.getPrice())) {
            throw new NotFoundException("Product does not have a price to update.");
        }
        Price updatedPrice=priceMapper.toPriceFromUpdatePriceDto(price);
        product.setPrice(updatedPrice);
        return priceMapper.toPriceDto(priceService.savePrice(updatedPrice));
    }
}
