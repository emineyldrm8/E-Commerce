package com.haratres.ecommerce.mapper;

import com.haratres.ecommerce.dto.*;
import com.haratres.ecommerce.model.Address;
import com.haratres.ecommerce.model.City;
import com.haratres.ecommerce.model.County;
import com.haratres.ecommerce.model.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE= Mappers.getMapper(AddressMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "county.id", target = "county.id")
    @Mapping(source = "city.id", target = "city.id")
    @Mapping(source = "district.id", target = "district.id")
    AddressDto toAddressDto(Address address);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "county.id", target = "county.id")
    @Mapping(source = "city.id", target = "city.id")
    @Mapping(source = "district.id", target = "district.id")
    Address fromAddressDto(AddressDto addressDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "county.id", target = "countyId")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "district.id", target = "districtId")
    CreateAddressDto toCreateAddressDto(Address address);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "countyId", target = "county.id")
    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "districtId", target = "district.id")
    Address fromCreateAddressDto(CreateAddressDto addressDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "county.id", target = "countyId")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "district.id", target = "districtId")
    UpdateAddressDto toUpdateAddressDto(Address address);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "countyId", target = "county.id")
    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "districtId", target = "district.id")
    Address fromUpdateAddressDto(UpdateAddressDto addressDto);

    List<AddressDto> toAddressDtoList(List<Address> addressList);
    List<Address> toAddressList(List<AddressDto> addressDtoList);

    CityDto toCityDto(City city);
    City toCity(CityDto cityDto);

    CountyDto toCountyDto(County county);
    County toCounty(CountyDto countyDto);

    DistrictDto toDistrictDto(District district);
    District toDistrict(DistrictDto districtDto);
}
