package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.StockDto;
import com.haratres.ecommerce.dto.UpdateStockDto;
import com.haratres.ecommerce.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    @Mapping(target = "productId", source = "product.id")
    StockDto toStockDto(Stock stock);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    Stock toStockFromUpdateStockDto(UpdateStockDto stockDto);
}
