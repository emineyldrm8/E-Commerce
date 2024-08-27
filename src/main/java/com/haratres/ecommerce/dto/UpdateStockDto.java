package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class UpdateStockDto {
    private Long id;
    private int quantity;
}
