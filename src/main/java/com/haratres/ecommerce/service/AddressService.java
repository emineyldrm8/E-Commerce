package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.DuplicateEntryException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.model.Address;
import com.haratres.ecommerce.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    public AddressService(AddressRepository addressRepository, UserService userService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    public Address getAddressByAddressId(Long userId, Long addressId) {
        validateAddressAccess(userId, addressId);
        return addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Address not found with id: {}", addressId);
                    return new NotFoundException("Address  not found with id: " + addressId);
                });
    }

    public List<Address> getAddressesByUserId(Long userId) {
        validateUserAccess(userId);
        return addressRepository.findAllByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Address  not found with by user idd: {}", userId);
                    return new NotFoundException("Address not found with by user id: " + userId);
                });
    }

    public Address saveAddress(Long userId, Address address) {
        validateAddressAccess(userId, address.getUser().getId());
        if (doesExistsAddress(userId, address)) {
            logger.error("The address with title '{}' already exists for user ID: {}. Existing address ID: {}",
                    address.getTitle(), userId, address.getId());
            throw new DuplicateEntryException(
                    "The address with title '" + address.getTitle() + "' already exists for user ID: " + userId +
                            ". Existing address ID: " + address.getId()
            );
        }
        return addressRepository.save(address);
    }

    public boolean doesExistsAddress(Long userId, Address address) {
        Optional<Address> existingAddress = addressRepository.findByCountryAndCityAndDistrictAndTextAndUserId(address.getCountry(),
                address.getCity(), address.getDistrict(),
                address.getText(), address.getUser().getId()
        );
        return existingAddress.isPresent();
    }

    public void deleteAddressByAddressId(Long userId, Long addressId) {
        validateAddressAccess(userId,addressId);
        Address address = getAddressByAddressId(userId, addressId);
        addressRepository.delete(address);
    }

    public void deleteAddresses(Long userId)
    {
        validateUserAccess(userId);
        List<Address> addresses= getAddressesByUserId(userId);
        addressRepository.deleteAll(addresses);
    }

    public Address updateAddress(Long userId, Long addressId, Address updatedAddress) {
        validateAddressAccess(userId, addressId);
        Address existingAddress = getAddressByAddressId(userId, addressId);
        existingAddress.setTitle(updatedAddress.getTitle());
        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setDistrict(updatedAddress.getDistrict());
        existingAddress.setText(updatedAddress.getText());
        return addressRepository.save(existingAddress);
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
