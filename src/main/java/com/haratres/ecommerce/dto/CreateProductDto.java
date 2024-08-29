package com.haratres.ecommerce.dto;

import lombok.Data;
@Data
public class CreateProductDto {
    private String name;
    private String code;
    private String title;
    private String description;
    private int stock;
    private double price;
    private String color;
    private String size;
}
