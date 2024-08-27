package com.haratres.ecommerce.dto;

import lombok.Data;

@Data
public class UpdateProductDto {
    private String name;
    private String code;
    private String title;
    private String description;
    private Double price;
    private int stock;
    private String color;
    private String size;
}
