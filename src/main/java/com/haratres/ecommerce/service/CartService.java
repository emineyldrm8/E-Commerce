package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.CartDto;
import com.haratres.ecommerce.dto.CartEntryDto;
import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.mapper.CartMapper;
import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.CartEntry;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.model.User;
import com.haratres.ecommerce.repository.CartEntryRepository;
import com.haratres.ecommerce.repository.CartRepository;
import com.haratres.ecommerce.repository.ProductRepository;
import com.haratres.ecommerce.repository.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartEntryRepository cartEntryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;
    private final CartMapper cartMapper = CartMapper.INSTANCE;


    public CartDto getOrCreateCart(Long userId, Long cartId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        if (Objects.isNull(existingCart)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found with user id: " + userId));
            Cart newCart = new Cart();
            newCart.setUser(user);
            Cart savedCart = cartRepository.save(newCart);
            return new CartDto(Collections.emptyList());
        } else {
            validateUserAccess(userId, existingCart.getUser().getId());
            Hibernate.initialize(existingCart.getCartEntries());
            List<CartEntryDto> cartEntryDtos = existingCart.getCartEntries().stream()
                    .map(entry -> new CartEntryDto(
                            entry.getProduct().getId(),
                            entry.getProduct().getName(),
                            entry.getQuantity()))
                    .collect(Collectors.toList());
            return new CartDto(cartEntryDtos);
        }
    }

    public CartDto increaseProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        Product product = getProductById(productId);
        Optional<CartEntry> existingEntry = existingCart.getCartEntries().stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst();
        if (existingEntry.isPresent()) {
            CartEntry cartEntry = existingEntry.get();
            int newQuantity = cartEntry.getQuantity() + quantity;
            if (newQuantity <= 0) {
                existingCart.getCartEntries().remove(cartEntry);
                cartEntryRepository.delete(cartEntry);
            } else {
                cartEntry.setQuantity(newQuantity);
                cartEntryRepository.save(cartEntry);
            }
        } else {
            CartEntry newEntry = new CartEntry();
            newEntry.setCart(existingCart);
            newEntry.setProduct(product);
            newEntry.setQuantity(quantity);
            cartEntryRepository.save(newEntry);
        }
        Cart updatedCart = cartRepository.save(existingCart);
        List<CartEntryDto> cartEntryDtos = updatedCart.getCartEntries().stream()
                .map(entry -> new CartEntryDto(
                        entry.getProduct().getId(),
                        entry.getProduct().getName(),
                        entry.getQuantity()))
                .collect(Collectors.toList());

        return new CartDto(cartEntryDtos);
    }

    public CartDto decreaseProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        Product product = getProductById(productId);
        Optional<CartEntry> existingEntry = existingCart.getCartEntries().stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst();
        if (existingEntry.isPresent()) {
            CartEntry cartEntry = existingEntry.get();
            int newQuantity = cartEntry.getQuantity() - quantity;
            if (newQuantity > 0) {
                cartEntry.setQuantity(newQuantity);
                cartEntryRepository.save(cartEntry);
            } else {
                existingCart.getCartEntries().remove(cartEntry);
                cartEntryRepository.delete(cartEntry);
            }
        } else {
            throw new NotFoundException("Product not found in cart " + product.getName());
        }
        Cart updatedCart = cartRepository.save(existingCart);
        List<CartEntryDto> cartEntryDtos = updatedCart.getCartEntries().stream()
                .map(entry -> new CartEntryDto(
                        entry.getProduct().getId(),
                        entry.getProduct().getName(),
                        entry.getQuantity()))
                .collect(Collectors.toList());

        return new CartDto(cartEntryDtos);
    }

    public CartDto removeProductFromCart(Long userId, Long cartId, Long productId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        existingCart.getCartEntries().removeIf(entry -> entry.getProduct().getId().equals(productId));
        cartRepository.save(existingCart);
        List<CartEntryDto> cartEntryDtos = existingCart.getCartEntries().stream()
                .map(entry -> new CartEntryDto(
                        entry.getProduct().getId(),
                        entry.getProduct().getName(),
                        entry.getQuantity()))
                .collect(Collectors.toList());
        return new CartDto(cartEntryDtos);
    }

    public CartDto updateProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        boolean productFound = false;
        for (CartEntry cartEntry : existingCart.getCartEntries()) {
            if (cartEntry.getProduct().getId().equals(productId)) {
                cartEntry.setQuantity(quantity);
                productFound = true;
                break;
            }
        }
        if (!productFound) {
            throw new NotFoundException("Product not found in cart entry: " + productId);
        }
        Cart updatedCart = cartRepository.save(existingCart);
        List<CartEntryDto> cartEntryDtos = updatedCart.getCartEntries().stream()
                .map(entry -> new CartEntryDto(
                        entry.getProduct().getId(),
                        entry.getProduct().getName(),
                        entry.getQuantity()))
                .collect(Collectors.toList());
        return new CartDto(cartEntryDtos);
    }


    public void deleteCart(Long userId, Long cartId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        String username = existingCart.getUser().getUsername();
        if (Objects.isNull(existingCart)) {
            logger.error("Cart not found for username {}", username);
            throw new NotFoundException("Cart not found for username: " + username);
        } else {
            cartRepository.delete(existingCart);
            logger.info("Cart deleted for username {}", username);
        }
    }

    public void deleteAllCartEntries(Long userId, Long cartId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        String username = existingCart.getUser().getUsername();
        if (Objects.isNull(existingCart)) {
            logger.error("Cart not found for username {}", username);
            throw new NotFoundException("Cart not found for username: " + username);
        } else {
            cartEntryRepository.deleteAll(existingCart.getCartEntries());
            cartRepository.save(existingCart);
            logger.info("Cart entries deleted for username {}", username);
        }
    }

    public Long getUserIdFromCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return userDetails.getId();
    }

    public void validateUserAccess(Long pathUserId, Long cartUserId) {
        Long currentUserId = getUserIdFromCurrentUser();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
        if (!cartUserId.equals(pathUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", id);
                    return new NotFoundException("Product not found with id:" + id);
                });
        logger.info("Found product with id: {}", id);
        return product;
    }

}
