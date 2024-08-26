package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class UpdateAddressDto {
    private Long userId;
    private Long cityId;
    private Long countyId;
    private Long districtId;
    private String title;
    private String text;
}
