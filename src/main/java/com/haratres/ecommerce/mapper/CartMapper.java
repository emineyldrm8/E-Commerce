
package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.CartDto;
import com.haratres.ecommerce.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ProductMapper.class)
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDto toCartDto(Cart cart);
}
