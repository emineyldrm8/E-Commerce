package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CartEntryDto;
import com.haratres.ecommerce.model.CartEntry;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ProductMapper.class)
public interface CartEntryMapper {
    CartEntryMapper INSTANCE = Mappers.getMapper(CartEntryMapper.class);

    CartEntryDto toCartEntryDto(CartEntry cartentry);
}
