package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.model.County;
import com.haratres.ecommerce.model.District;
import com.haratres.ecommerce.repository.DistrictRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    private final Logger logger = LoggerFactory.getLogger(DistrictService.class);

    public District saveDistrict(District district) {
        try {
            return districtRepository.save(district);
        } catch (Exception e) {
            logger.error("Failed to save district {}",district);
            throw new NotSavedException("Failed to save district: " + district, e);
        }
    }

    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    public District getDistrictById(Long districtId) {
        return districtRepository.findById(districtId)
                .orElseThrow(() -> {
                    logger.error("District not found with id: {}", districtId);
                    return new NotFoundException("District not found with id: " + districtId);
                });
    }
}
