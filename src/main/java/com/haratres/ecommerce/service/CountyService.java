package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.model.County;
import com.haratres.ecommerce.repository.CountyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountyService {

    @Autowired
    private CountyRepository countyRepository;
    private final Logger logger = LoggerFactory.getLogger(CountyService.class);

    public County saveCounty(County county) {
        try {
            return countyRepository.save(county);
        } catch (Exception e) {
            logger.error("Failed to save county {}",county);
            throw new NotSavedException("Failed to save county: " + county, e);
        }
    }

    public List<County> getAllCounties() {
        return countyRepository.findAll();
    }

    public County getCountyById(Long countyId) {
        return countyRepository.findById(countyId)
                .orElseThrow(() -> {
                    logger.error("County not found with id: {}", countyId);
                    return new NotFoundException("County not found with id: " + countyId);
                });
    }
}
