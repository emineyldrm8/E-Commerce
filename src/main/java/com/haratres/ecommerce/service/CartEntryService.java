package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotDeletedException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.CartEntry;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.repository.CartEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartEntryService {

    private final Logger logger = LoggerFactory.getLogger(CartEntryService.class);
    private final CartEntryRepository cartEntryRepository;

    public CartEntryService(CartEntryRepository cartEntryRepository) {
        this.cartEntryRepository = cartEntryRepository;
    }

    public CartEntry saveCartEntry(CartEntry cartEntry) {
        try {
            return cartEntryRepository.save(cartEntry);
        } catch (Exception e) {
            logger.error("Failed to save CartEntry: {}", cartEntry);
            throw new NotSavedException("Failed to save CartEntry: " + cartEntry, e);
        }
    }

    public void deleteCartEntry(CartEntry cartEntry) {
        try {
            cartEntryRepository.delete(cartEntry);
        } catch (Exception e) {
            logger.error("Failed to delete CartEntry with id: {}", cartEntry.getId());
            throw new NotDeletedException("Failed to delete CartEntry with id: " + cartEntry.getId(), e);
        }
    }

    public void deleteAllCartEntries(List<CartEntry> cartEntries) {
        try {
            cartEntryRepository.deleteAll(cartEntries);
        } catch (Exception e) {
            logger.error("Failed to delete CartEntryList");
            throw new NotDeletedException("Failed to delete CartEntryList", e);
        }
    }

    public Optional<CartEntry> findCartEntryByCartAndProduct(Cart cart, Product product) {
        return cartEntryRepository.findByCartAndProduct(cart, product);
    }

    public void deleteCartEntryByCartAndProduct(Cart cart, Product product) {
        Optional<CartEntry> cartEntryOptional = findCartEntryByCartAndProduct(cart, product);
        if (cartEntryOptional.isPresent()) {
            deleteCartEntry(cartEntryOptional.get());
        } else {
            logger.error("Cart entry not found for deletion.");
            throw new NotFoundException("Cart entry not found for deletion.");
        }
    }
}
