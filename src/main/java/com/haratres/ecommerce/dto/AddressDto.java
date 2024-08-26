package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private Long userId;
    private CityDto city;
    private CountyDto county;
    private DistrictDto district;
    private String title;
    private String text;
}
