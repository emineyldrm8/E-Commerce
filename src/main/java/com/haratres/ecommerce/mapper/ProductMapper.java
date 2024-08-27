
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

    @Mapping(source ="price", target = "price.value")
    Product toProductFromCreateProductDto(CreateProductDto createProductDto);

    @Mapping(source ="price.value", target = "price")
    CreateProductDto toCreateProductDto(Product product);

    @Mapping(source = "price", target = "price.value")
    Product toProductFromUpdateProductDto(UpdateProductDto productDto);

    @Mapping(source ="price.value", target = "price")
    UpdateProductDto toUpdateProductDto(Product product);

    List<ProductDto> toProductDtoList(List<Product> productList);

    List<Product> toProductListFromCreate(List<CreateProductDto> productDtoList);

    @Mapping(target = "productId", source = "product.id")
    PriceDto toPriceDto(Price price);

    @Mapping(target = "product.id", source = "productId")
    Price toPrice(PriceDto priceDto);

    UpdatePriceDto toUpdatePriceDto(Price price);

    @Mapping(target = "product", ignore = true)
    Price toPriceFromUpdatePriceDto(UpdatePriceDto priceDto);

    CreatePriceDto toCreatePriceDto(Price price);

    @Mapping(target = "product", ignore = true)
    Price toPriceFromCreatePriceDto(CreatePriceDto priceDto);

    List<PriceDto> toPriceDtoList(List<Price> prices);

    List<Price> toPriceList(List<PriceDto> priceDtos);
}