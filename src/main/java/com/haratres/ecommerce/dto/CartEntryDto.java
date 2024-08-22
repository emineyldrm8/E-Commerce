package com.haratres.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CartEntryDto {
    private Long productId;
    private String productName;
    private Integer quantity;
}
