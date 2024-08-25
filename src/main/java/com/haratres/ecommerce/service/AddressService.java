package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.AddressDto;
import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.DuplicateEntryException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.mapper.AddressMapper;
import com.haratres.ecommerce.mapper.CartMapper;
import com.haratres.ecommerce.model.Address;
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

    public AddressService(AddressRepository addressRepository, UserService userService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    public AddressDto getAddressByAddressId(Long userId, Long addressId) {
        validateAddressAccess(userId, addressId);
        Address address=addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Address not found with id: {}", addressId);
                    return new NotFoundException("Address  not found with id: " + addressId);
                });
         return addressMapper.toAddressDto(address);
    }

    public List<AddressDto> getAddressesByUserId(Long userId) {
        validateUserAccess(userId);
        List<Address> addresses=addressRepository.findAllByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Address  not found with by user idd: {}", userId);
                    return new NotFoundException("Address not found with by user id: " + userId);
                });
        return addressMapper.toAddressDtoList(addresses);
    }

    public AddressDto saveAddress(Long userId, AddressDto addressDto) {
        Address address=addressMapper.toAddress(addressDto);
        validateAddressAccess(userId, address.getUser().getId());
        if (doesExistsAddress(address)) {
            logger.error("The address with title '{}' already exists for user ID: {}. Existing address ID: {}",
                    address.getTitle(), userId, address.getId());
            throw new DuplicateEntryException(
                    "The address with title '" + address.getTitle() + "' already exists for user ID: " + userId +
                            ". Existing address ID: " + address.getId()
            );
        }
        return addressMapper.toAddressDto(addressRepository.save(address));
    }

    public boolean doesExistsAddress(Address address) {
        return addressRepository.existsByCountyAndCityAndDistrictAndTitleAndUserId(
                address.getCounty(),
                address.getCity(),
                address.getDistrict(),
                address.getTitle(),
                address.getUser().getId()
        );
    }

    public void deleteAddressByAddressId(Long userId, Long addressId) {
        validateAddressAccess(userId,addressId);
        AddressDto address = getAddressByAddressId(userId, addressId);
        addressRepository.delete(addressMapper.toAddress(address));
    }

    public void deleteAddresses(Long userId)
    {
        validateUserAccess(userId);
        List<AddressDto> addresses= getAddressesByUserId(userId);
        addressRepository.deleteAll(addressMapper.toAddressList(addresses));
    }

    public AddressDto updateAddress(Long userId, Long addressId, AddressDto updatedAddress) {
        validateAddressAccess(userId, addressId);
        AddressDto existingAddress = getAddressByAddressId(userId, addressId);
        existingAddress.setTitle(updatedAddress.getTitle());
        existingAddress.setCountyId(updatedAddress.getCountyId());
        existingAddress.setCityId(updatedAddress.getCityId());
        existingAddress.setDistrictId(updatedAddress.getDistrictId());
        existingAddress.setText(updatedAddress.getText());
        return addressMapper.toAddressDto(addressRepository.save(addressMapper.toAddress(existingAddress)));
    }

    private void validateAddressAccess(Long pathUserId, Long addressId) {
        Long currentUserId = userService.getCurrentUser().getId();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
        if (!addressId.equals(pathUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
    }

    private void validateUserAccess(Long pathUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this resource.");
        }
    }
}
