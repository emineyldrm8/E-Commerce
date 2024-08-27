package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CreatePriceDto;
import com.haratres.ecommerce.dto.PriceDto;
import com.haratres.ecommerce.dto.ProductDto;
import com.haratres.ecommerce.dto.UpdatePriceDto;
import com.haratres.ecommerce.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    @Mapping(target = "productId", source = "product.id")
    PriceDto toPriceDto(Price price);

    @Mapping(target = "product.id", source = "productId")
    Price toPrice(PriceDto priceDto);

    @Mapping(target = "productId", source = "product.id")
    UpdatePriceDto toUpdatePriceDto(Price price);

    @Mapping(target = "product.id", source = "productId")
    Price toPriceFromUpdatePriceDto(UpdatePriceDto priceDto);

    @Mapping(target = "productId", source = "product.id")
    CreatePriceDto toCreatePriceDto(Price price);

    @Mapping(target = "product.id", source = "productId")
    Price toPriceFromCreatePriceDto(CreatePriceDto priceDto);

}
