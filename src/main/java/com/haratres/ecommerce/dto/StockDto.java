package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class StockDto {
    private Long id;
    private int quantity;
    private Long productId;
}
