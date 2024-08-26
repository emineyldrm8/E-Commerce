package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Address;
import com.haratres.ecommerce.model.City;
import com.haratres.ecommerce.model.County;
import com.haratres.ecommerce.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByUserId(Long userId);
    boolean existsByCountyAndCityAndDistrictAndTitleAndUserId(
            County county,
            City city,
            District district,
            String title,
            Long userId
    );
}
