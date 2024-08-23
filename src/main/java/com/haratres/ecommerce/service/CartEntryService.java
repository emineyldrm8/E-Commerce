package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.repository.CartEntryRepository;
import com.haratres.ecommerce.repository.CartRepository;
import com.haratres.ecommerce.repository.ProductRepository;
import com.haratres.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CartEntryService {
    private final Logger logger = LoggerFactory.getLogger(CartEntryService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartEntryRepository cartEntryRepository;

    @Autowired
    private CartService cartService;

    public void deleteAllCartEntries(Long userId, Long cartId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        cartService.validateUserAccess(userId, existingCart.getUser().getId());
        String username = existingCart.getUser().getUsername();
        cartEntryRepository.deleteAll(existingCart.getCartEntries());
        cartRepository.save(existingCart);
        logger.info("Cart entries deleted for username {}", username);
    }
}
