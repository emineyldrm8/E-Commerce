package com.haratres.ecommerce.controller;
import com.haratres.ecommerce.model.Address;
import com.haratres.ecommerce.repository.AddressRepository;
import com.haratres.ecommerce.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/address")
public class AddressController {
    private final Logger logger= LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;
    private final AddressRepository addressRepository;

    public AddressController(AddressService addressService,
                             AddressRepository addressRepository) {
        this.addressService = addressService;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddress(@PathVariable Long addressId, @RequestParam Long userId) {
        Address address = addressService.getAddressByAddressId(userId, addressId);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAddresses(@PathVariable Long userId) {
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<Address> createAdress(@PathVariable Long userId,@RequestBody Address address) {
        Address saveAddress=addressService.saveAddress(userId, address);
        return new ResponseEntity<>(saveAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address>  updateAddress(@PathVariable Long userId,@PathVariable Long addressId,@RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(userId, addressId,address);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        addressService.deleteAddressByAddressId(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAddresses(@PathVariable Long userId) {
        addressService.deleteAddresses(userId);
        return ResponseEntity.noContent().build();
    }
}
