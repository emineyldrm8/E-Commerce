package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.AddressDto;
import com.haratres.ecommerce.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE= Mappers.getMapper(AddressMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "county.id", target = "countyId")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "district.id", target = "districtId")
    AddressDto toAddressDto(Address address);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "countyId", target = "county.id")
    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "districtId", target = "district.id")
    Address toAddress(AddressDto addressDto);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "county.id", target = "countyId")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "district.id", target = "districtId")
    List<AddressDto> toAddressDtoList(List<Address> addressList);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "countyId", target = "county.id")
    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "districtId", target = "district.id")
    List<Address> toAddressList(List<AddressDto> addressDtoList);
}
