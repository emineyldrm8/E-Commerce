package com.haratres.ecommerce.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private List<CartEntryDto> cartEntries;
}
