package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private String title;
    private String description;
    private PriceDto price;
    private String color;
    private String size;
}
