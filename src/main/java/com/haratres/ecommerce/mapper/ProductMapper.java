package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE= Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);

    List<Product> toProductList(List<ProductDto> productDtoList);
    List<ProductDto> toProductDtoList(List<Product> productList);
}
