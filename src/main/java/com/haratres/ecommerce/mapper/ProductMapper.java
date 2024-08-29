package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.*;
import com.haratres.ecommerce.model.Price;
import com.haratres.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);

    Product toProduct(ProductDto productDto);

    @Mapping(source ="price.value", target = "price")
    @Mapping(source ="stock", target = "stock.quantity")
    Product toProductFromCreateProductDto(CreateProductDto createProductDto);

    @Mapping(source ="stock.quantity", target = "stock")
    CreateProductDto toCreateProductDto(Product product);

    @Mapping(source ="stock.quantity", target = "stock")
    @Mapping(source = "price", target = "price.value")
    Product toProductFromUpdateProductDto(UpdateProductDto productDto);

    @Mapping(source ="price.value", target = "price")
    UpdateProductDto toUpdateProductDto(Product product);

    List<ProductDto> toProductDtoList(List<Product> productList);

    List<Product> toProductListFromCreate(List<CreateProductDto> productDtoList);

}