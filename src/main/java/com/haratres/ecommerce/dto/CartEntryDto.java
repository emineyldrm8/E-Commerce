package com.haratres.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class CartEntryDto {
    private ProductDto product;
    private Integer quantity;
}
