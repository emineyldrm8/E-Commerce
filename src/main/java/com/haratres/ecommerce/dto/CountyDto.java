package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class CountyDto {
    private Long id;
    private String name;
    private CityDto city;
}
