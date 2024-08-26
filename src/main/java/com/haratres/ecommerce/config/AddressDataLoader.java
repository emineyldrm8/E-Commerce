package com.haratres.ecommerce.config;

import com.haratres.ecommerce.model.City;
import com.haratres.ecommerce.model.County;
import com.haratres.ecommerce.model.District;
import com.haratres.ecommerce.service.CityService;
import com.haratres.ecommerce.service.CountyService;
import com.haratres.ecommerce.service.DistrictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class AddressDataLoader {

    @Autowired
    private CityService cityService;

    @Autowired
    private CountyService countyService;

    @Autowired
    private DistrictService districtService;

    @Value("${address.data.filepath}")
    private String addressDataFilePath;

    private final Logger logger = LoggerFactory.getLogger(AddressDataLoader.class);

    @Bean
    public CommandLineRunner initAddressData(CityService cityService, CountyService countyService, DistrictService districtService) {
        return args -> {
            if (cityService.getAllCities().isEmpty()) {
                loadCitiesData();
            } else {
                logger.info("Address data already loaded, no upload");
            }
        };
    }

    public void loadCitiesData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<City> cities = mapper.readValue(new File(addressDataFilePath), new TypeReference<List<City>>() {
            });
            for (City cityData : cities) {
                City city = new City();
                city.setName(cityData.getName());
                city = cityService.saveCity(city);

                for (County countyData : cityData.getCounties()) {
                    County county = new County();
                    county.setName(countyData.getName());
                    county.setCity(city);
                    county = countyService.saveCounty(county);

                    for (District districtData : countyData.getDistricts()) {
                        District district = new District();
                        district.setName(districtData.getName());
                        district.setCounty(county);
                        districtService.saveDistrict(district);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
