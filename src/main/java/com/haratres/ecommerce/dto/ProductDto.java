package com.haratres.ecommerce.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "products")
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private String title;
    private String description;
    private int stock;
    private double price;
    private String color;
    private String size;
}
