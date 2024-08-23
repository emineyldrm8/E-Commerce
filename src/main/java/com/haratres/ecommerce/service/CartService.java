package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.CartDto;
import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.InvalidQuantityException;
import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.exception.NotSavedException;
import com.haratres.ecommerce.mapper.CartMapper;
import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.model.CartEntry;
import com.haratres.ecommerce.model.Product;
import com.haratres.ecommerce.model.User;
import com.haratres.ecommerce.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {
    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartEntryService cartEntryService;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartRepository cartRepository,CartEntryService cartEntryService, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartEntryService = cartEntryService;
        this.productService = productService;
        this.userService = userService;
    }

    private final CartMapper cartMapper = CartMapper.INSTANCE;



    public CartDto getOrCreateCart(Long userId) {
        Cart existingCart = cartRepository.findByUserId(userId)
                .orElse(null);
        if (Objects.isNull(existingCart)) {
            User user = userService.getUserById(userId);
            Cart newCart = new Cart();
            newCart.setUser(user);
            Cart savedCart =saveCart(newCart);
            return cartMapper.toCartDto(savedCart);
        } else {
            validateUserAccess(userId, existingCart.getUser().getId());
            return cartMapper.toCartDto(existingCart);
        }
    }

    public CartDto increaseProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = findCartById(cartId);
        validateUserAccess(userId, existingCart.getUser().getId());
        Product product = productService.getProductById(productId);
        Optional<CartEntry> existingEntry = cartEntryService.findCartEntryByCartAndProduct(existingCart, product);
        if (existingEntry.isPresent()) {
            CartEntry cartEntry = existingEntry.get();
            int newQuantity = cartEntry.getQuantity() + quantity;
                cartEntry.setQuantity(newQuantity);
                cartEntryService.saveCartEntry(cartEntry);
        } else {
            CartEntry newEntry = new CartEntry();
            newEntry.setCart(existingCart);
            newEntry.setProduct(product);
            newEntry.setQuantity(quantity);
            cartEntryService.saveCartEntry(newEntry);
        }
        return cartMapper.toCartDto(saveCart(existingCart));
    }

    public CartDto decreaseProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = findCartById(cartId);
        validateUserAccess(userId, existingCart.getUser().getId());
        Product product = productService.getProductById(productId);
        Optional<CartEntry> existingEntry = cartEntryService.findCartEntryByCartAndProduct(existingCart, product);
        if (existingEntry.isPresent()) {
            CartEntry cartEntry = existingEntry.get();
            int newQuantity = cartEntry.getQuantity() - quantity;
            if (newQuantity > 0) {
                cartEntry.setQuantity(newQuantity);
                cartEntryService.saveCartEntry(cartEntry);
            } else {
                existingCart.getCartEntries().remove(cartEntry);
                cartEntryService.deleteCartEntry(cartEntry);
            }
        } else {
            throw new NotFoundException("Product not found in cart " + product.getName());
        }
        return cartMapper.toCartDto(saveCart(existingCart));
    }

    public CartDto removeProductFromCart(Long userId, Long cartId, Long productId) {
        Cart existingCart = findCartById(cartId);
        validateUserAccess(userId, existingCart.getUser().getId());
        deleteCartEntryByCartAndProduct(existingCart,productService.getProductById(productId));
        return cartMapper.toCartDto(saveCart(existingCart));
    }

    public CartDto updateProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = findCartById(cartId);
        validateUserAccess(userId, existingCart.getUser().getId());
        Product product=productService.getProductById(productId);
        Optional<CartEntry> existingEntry=cartEntryService.findCartEntryByCartAndProduct(existingCart,product);
        if (existingEntry.isPresent()) {
            CartEntry cartEntry = existingEntry.get();
            if (quantity > 0) {
                cartEntry.setQuantity(quantity);
                cartEntryService.saveCartEntry(cartEntry);
            } else {
                logger.error("Quantity must be greater than zero.");
                throw new InvalidQuantityException("Quantity must be greater than zero.");
            }
        } else {
            throw new NotFoundException("Product not found in cart " + product.getName());
        }
        return cartMapper.toCartDto(saveCart(existingCart));
    }

    public void deleteAllCartEntries(Long userId, Long cartId) {
        Cart existingCart = findCartById(cartId);;
        validateUserAccess(userId, existingCart.getUser().getId());
        cartEntryService.deleteAllCartEntries(existingCart.getCartEntries());
        saveCart(existingCart);
    }

    public void deleteCart(Long userId, Long cartId) {
        Cart existingCart = findCartById(cartId);
        validateUserAccess(userId, existingCart.getUser().getId());
        cartRepository.delete(existingCart);
    }

    public void deleteCartEntryByCartAndProduct(Cart cart, Product product) {
        Optional<CartEntry> cartEntryOptional = cartEntryService.findCartEntryByCartAndProduct(cart, product);
        if(cartEntryOptional.isPresent())
        {
            cartEntryService.deleteCartEntry(cartEntryOptional.get());
        }else{
            logger.error("Cart entry not found for deletion.");
            throw new NotFoundException("Cart entry not found for deletion.");
        }
    }

    private void validateUserAccess(Long pathUserId, Long cartUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
        if (!cartUserId.equals(pathUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
    }

    public Cart saveCart(Cart cart) {
        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("Failed to save Cart: {}", cart);
            throw new NotSavedException("Failed to save Cart: " + cart, e);
        }
    }

    public Cart findCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    logger.error("Cart not found with id: {}", cartId);
                    return new NotFoundException("Cart not found with id: " + cartId);
                });
    }

}
