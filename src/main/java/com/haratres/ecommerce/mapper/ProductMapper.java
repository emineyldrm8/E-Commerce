package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CreateProductDto;
import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UpdateProductDto;
import com.haratres.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE= Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);

    CreateProductDto toCreateProductDto(Product product);
    Product toProduct(CreateProductDto createProductDto);

    UpdateProductDto toUpdateProductDto(Product product);
    Product toProduct(UpdateProductDto productDto);

    List<Product> toProductList(List<ProductDto> productDtoList);
    List<ProductDto> toProductDtoList(List<Product> productList);

    List<Product> toProductListFromCreate(List<CreateProductDto> productDtoList);
    List<CreateProductDto> toCreateProductDtoList(List<Product> productList);

    List<Product> toProductListFromUpdate(List<UpdateProductDto> productDtoList);
    List<ProductDto> toUpdateProductDtoList(List<Product> productList);
}
