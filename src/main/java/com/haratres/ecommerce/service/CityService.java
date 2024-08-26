package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.model.City;
import com.haratres.ecommerce.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    private final Logger logger = LoggerFactory.getLogger(CityService.class);

    public City saveCity(City city) {
        try {
            return cityRepository.save(city);
        } catch (Exception e) {
            logger.error("Failed to save city {}", city);
            throw new NotSavedException("Failed to save city: " + city, e);
        }
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> {
                    logger.error("City not found with id: {}", cityId);
                    return new NotFoundException("City not found with id: " + cityId);
                });
    }
}
