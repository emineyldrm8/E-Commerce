package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CartDto;
import com.haratres.ecommerce.dto.CartEntryDto;
import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.CartEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toCartDto(Cart cart);
    Cart toCart(CartDto cartDto);
    List<CartEntryDto> toCartEntryDtoList(List<CartEntry> cartEntries);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product", target = "product")
    CartEntryDto toCartEntryDto(CartEntry cartEntry);
}
