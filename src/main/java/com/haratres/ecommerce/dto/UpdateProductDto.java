package com.haratres.ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateProductDto {
    private String title;
    private String description;
    private Double price;
    private String color;
    private String size;
}
