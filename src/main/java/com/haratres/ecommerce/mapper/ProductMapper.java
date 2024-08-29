package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CreateProductDto;
import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UpdateProductDto;
import com.haratres.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE= Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "stock.quantity", source = "stock")
    @Mapping(target = "price.value", source = "price")
    ProductDto toProductDto(Product product);
    @Mapping(target = "stock", source = "stock.quantity")
    @Mapping(target = "price", source = "price.value")
    Product toProduct(ProductDto productDto);

    @Mapping(target = "stock", source = "stock.quantity")
    @Mapping(target = "price", source = "price.value")
    CreateProductDto toCreateProductDto(Product product);
    @Mapping(target = "stock.quantity", source = "stock")
    @Mapping(target = "price.value", source = "price")
    Product toProductFromCreateProductDto(CreateProductDto createProductDto);

    @Mapping(target = "stock", source = "stock.quantity")
    @Mapping(target = "price", source = "price.value")
    UpdateProductDto toUpdateProductDto(Product product);
    @Mapping(target = "stock.quantity", source = "stock")
    @Mapping(target = "price.value", source = "price")
    Product toProductFromUpdateProductDto(UpdateProductDto productDto);

    List<ProductDto> toProductDtoList(List<Product> productList);

    List<Product> toProductListFromCreate(List<CreateProductDto> productDtoList);
}
