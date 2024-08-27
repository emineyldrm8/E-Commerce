
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
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);

    @Mapping(source ="stock", target = "stock.quantity")
    Product toProductFromCreateProductDto(CreateProductDto createProductDto);

    @Mapping(source ="stock.quantity", target = "stock")
    CreateProductDto toCreateProductDto(Product product);

    @Mapping(source = "stock", target = "stock.quantity")
    Product toProductFromUpdateProductDto(UpdateProductDto productDto);

    @Mapping(source ="stock.quantity", target = "stock")
    UpdateProductDto toUpdateProductDto(Product product);

    List<ProductDto> toProductDtoList(List<Product> productList);

    List<Product> toProductListFromCreate(List<CreateProductDto> productDtoList);
}