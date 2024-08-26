package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class DistrictDto {
    private Long id;
    private String name;
    private CountyDto county;
}
