package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.StockDto;
import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.NotDeletedException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.mapper.StockMapper;
import com.haratres.ecommerce.model.Stock;
import com.haratres.ecommerce.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final Logger logger = LoggerFactory.getLogger(StockService.class);
    private final StockMapper stockMapper = StockMapper.INSTANCE;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockDto getStockByProductId(Long productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    logger.error("Stock not found with id: {}", productId);
                    return new NotFoundException("Stock not found with id: " + productId);
                });
        return stockMapper.toStockDto(stock);
    }

    public List<StockDto> getStocks() {
        return stockMapper.toStockDtoList(stockRepository.findAll());
    }

    public Stock saveStock(Stock stock) {
        try {
            return stockRepository.save(stock);
        } catch (Exception e) {
            throw new NotSavedException("Failed to save stock", e);
        }
    }

    public void deleteStock(Stock stock) {
        try {
            stockRepository.delete(stock);
        } catch (Exception e) {
            throw new NotDeletedException("Failed to delete stock", e);
        }
    }

    public void deleteAllStocks() {
        try {
            stockRepository.deleteAll();
        } catch (Exception e) {
            throw new NotDeletedException("Failed to delete stocks", e);
        }
    }
}
