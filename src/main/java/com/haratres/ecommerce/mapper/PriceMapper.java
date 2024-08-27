package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CreatePriceDto;
import com.haratres.ecommerce.dto.PriceDto;
import com.haratres.ecommerce.dto.ProductDto;
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

    UpdatePriceDto toUpdatePriceDto(Price price);

    @Mapping(target = "product", ignore = true)
    Price toPriceFromUpdatePriceDto(UpdatePriceDto priceDto);

    CreatePriceDto toCreatePriceDto(Price price);

    @Mapping(target = "product", ignore = true)
    Price toPriceFromCreatePriceDto(CreatePriceDto priceDto);

    List<PriceDto> toPriceDtoList(List<Price> prices);

    List<Price> toPriceList(List<PriceDto> priceDtos);


}
