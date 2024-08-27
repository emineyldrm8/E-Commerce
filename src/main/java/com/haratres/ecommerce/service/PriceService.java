package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.AddressDto;
import com.haratres.ecommerce.dto.CreateAddressDto;
import com.haratres.ecommerce.dto.PriceDto;
import com.haratres.ecommerce.dto.UpdateAddressDto;
import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.mapper.AddressMapper;
import com.haratres.ecommerce.mapper.PriceMapper;
import com.haratres.ecommerce.model.*;
import com.haratres.ecommerce.repository.AddressRepository;
import com.haratres.ecommerce.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class PriceService {
    private final PriceRepository priceRepository;
    private final Logger logger = LoggerFactory.getLogger(PriceService.class);
    private final PriceMapper priceMapper = PriceMapper.INSTANCE;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public PriceDto getPriceByProductId(Long productId) {
        Price price = priceRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    logger.error("Price not found with id: {}", productId);
                    return new NotFoundException("Price not found with id: " + productId);
                });
        return priceMapper.toPriceDto(price);
    }

    public Price savePrice(Price price) {
        try{
            return priceRepository.save(price);
        }
        catch(Exception e)
        {
            throw new NotSavedException("Failed to save price", e);
        }
    }


}
