package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.AddressDto;
import com.haratres.ecommerce.dto.CreateAddressDto;
import com.haratres.ecommerce.dto.UpdateAddressDto;
import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.DuplicateEntryException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.mapper.AddressMapper;
import com.haratres.ecommerce.model.Address;
import com.haratres.ecommerce.model.City;
import com.haratres.ecommerce.model.County;
import com.haratres.ecommerce.model.District;
import com.haratres.ecommerce.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AddressService.class);
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;
    private final CityService cityService;
    private final CountyService countyService;
    private final DistrictService districtService;

    public AddressService(AddressRepository addressRepository, UserService userService, CityService cityService, CountyService countyService, DistrictService districtService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.cityService = cityService;
        this.countyService = countyService;
        this.districtService = districtService;
    }


    public AddressDto getAddressByAddressId(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Address not found with id: {}", addressId);
                    return new NotFoundException("Address  not found with id: " + addressId);
                });
        validateUserAccess(userId, address.getUser().getId());
        return addressMapper.toAddressDto(address);
    }

    public Address getAddressModelByAddressId(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Address not found with id: {}", addressId);
                    return new NotFoundException("Address not found with id: " + addressId);
                });
        validateUserAccess(userId, address.getUser().getId());
        return address;
    }


    public List<AddressDto> getAddressesByUserId(Long userId) {
        validateCurrentUserMatchesPathUser(userId);
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return addressMapper.toAddressDtoList(addresses);
    }

    public AddressDto saveAddress(Long userId, CreateAddressDto addressDto) {
        try {
            Address address = addressMapper.fromCreateAddressDto(addressDto);
            validateUserAccess(userId, address.getUser().getId());
            City newCity = cityService.getCityById(addressDto.getCityId());
            County newCounty = countyService.getCountyById(addressDto.getCountyId());
            District newDistrict = districtService.getDistrictById(addressDto.getDistrictId());
            address.setCounty(newCounty);
            address.setCity(newCity);
            address.setDistrict(newDistrict);
            address.setTitle(addressDto.getTitle());
            address.setText(addressDto.getText());
            return addressMapper.toAddressDto(addressRepository.save(address));
        } catch (Exception e) {
            throw new NotSavedException("Failed to save address", e);
        }
    }

    public void deleteAddressByAddressId(Long userId, Long addressId) {
        Address address = getAddressModelByAddressId(userId, addressId);
        addressRepository.delete(address);
    }

    public void deleteAddresses(Long userId) {
        List<AddressDto> addresses = getAddressesByUserId(userId);
        addressRepository.deleteAll(addressMapper.toAddressList(addresses));
    }

    public AddressDto updateAddress(Long userId, Long addressId, UpdateAddressDto updatedAddress) {
        try {
            Address existingAddress = getAddressModelByAddressId(userId, addressId);
            City newCity = cityService.getCityById(updatedAddress.getCityId());
            County newCounty = countyService.getCountyById(updatedAddress.getCountyId());
            District newDistrict = districtService.getDistrictById(updatedAddress.getDistrictId());
            existingAddress.setCounty(newCounty);
            existingAddress.setCity(newCity);
            existingAddress.setDistrict(newDistrict);
            existingAddress.setTitle(updatedAddress.getTitle());
            existingAddress.setText(updatedAddress.getText());
            return addressMapper.toAddressDto(addressRepository.save(existingAddress));
        } catch (Exception e) {
            throw new NotSavedException("Failed to save address", e);
        }
    }

    private void validateUserAccess(Long pathUserId, Long addressUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this address");
        }
        if (!addressUserId.equals(pathUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this address");
        }
    }
    public void validateCurrentUserMatchesPathUser(Long pathUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this address");
        }
    }

}
