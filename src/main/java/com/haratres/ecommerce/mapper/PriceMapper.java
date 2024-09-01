package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CreatePriceDto;
import com.haratres.ecommerce.dto.PriceDto;
import com.haratres.ecommerce.dto.UpdatePriceDto;
import com.haratres.ecommerce.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    @Mapping(target = "productId", source = "product.id")
    PriceDto toPriceDto(Price price);

    @Mapping(target = "product.id", source = "productId")
    Price toPrice(PriceDto priceDto);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    Price toPriceFromUpdatePriceDto(UpdatePriceDto priceDto);
}
