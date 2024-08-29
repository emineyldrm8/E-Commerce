package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class PriceDto {
    private Long id;
    private Double value;
    private Long productId;
}
