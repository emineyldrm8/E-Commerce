package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.*;
import com.haratres.ecommerce.exception.*;
import com.haratres.ecommerce.mapper.PriceMapper;
import com.haratres.ecommerce.mapper.ProductMapper;
import com.haratres.ecommerce.mapper.StockMapper;
import com.haratres.ecommerce.model.Price;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.model.Stock;
import com.haratres.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final PaginationService paginationService;
    private final PriceService priceService;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final StockMapper stockMapper = StockMapper.INSTANCE;
    private final PriceMapper priceMapper = PriceMapper.INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, StockService stockService, PaginationService paginationService, PriceService priceService) {
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.paginationService = paginationService;
        this.priceService = priceService;
    }

    public List<ProductDto> getAllProducts() {
        return productMapper.toProductDtoList(productRepository.findAll());
    }

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

    public Page<ProductDto> searchProducts(PageRequestDto dto, String text) {
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
        updatedStock.setProduct(product);
        updatedStock = stockService.saveStock(updatedStock);
        product.setStock(updatedStock);
        productRepository.save(product);
        return stockMapper.toStockDto(updatedStock);
    }

    public void deleteStock(Long productId) {
        try {
            Product product = getProductById(productId);
            stockService.deleteStock(product.getStock());
            product.setStock(null);
            productRepository.save(product);
        } catch (Exception e) {
            logger.error("Failed to delete stock with id: {}", productId);
            throw new NotDeletedException("Failed to delete stock with id: " + productId, e);
        }
    }

    public PriceDto createPriceForProduct(Long productId, CreatePriceDto priceDto) {
        try {
            Product product = getProductById(productId);
            Price existingPrice = product.getPrice();
            if (Objects.equals(existingPrice.getValue(), 0)) {
                existingPrice.setValue(priceDto.getValue());
            } else {
                throw new NotSavedException("This product already has price. You cannot create another one.");
            }
            Price updatedPrice = priceService.savePrice(existingPrice);
            return priceMapper.toPriceDto(updatedPrice);
        } catch (Exception e) {
            throw new NotSavedException("An error occurred while processing the price for product ID: " + productId, e);
        }
    }

    public PriceDto updatePrice(Long productId, UpdatePriceDto price) {
        Product product = getProductById(productId);
        if (Objects.isNull(product.getPrice())) {
            throw new NotFoundException("Product does not have a price to update.");
        }
        Price updatedPrice = priceMapper.toPriceFromUpdatePriceDto(price);
        updatedPrice.setId(product.getPrice().getId());
        updatedPrice.setProduct(product);
        updatedPrice = priceService.savePrice(updatedPrice);
        product.setPrice(updatedPrice);
        productRepository.save(product);
        return priceMapper.toPriceDto(updatedPrice);
    }

    public void deletePrice(Long productId) {
        try {
            Product product = getProductById(productId);
                priceService.deletePrice(product.getPrice());
                product.setPrice(null);
                productRepository.save(product);
        } catch (Exception e) {
            logger.error("Failed to delete price with id: {}", productId);
            throw new NotDeletedException("Failed to delete price with id: " + productId, e);
        }
    }
}
