package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.AccessDeniedException;
import com.haratres.ecommerce.exception.NotFoundException;
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
import java.util.Objects;
import java.util.Optional;

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

    //kart oluşturma
    public Cart getOrCreateCart(String username) {
        User user = findUserByUsername(username);
        return cartRepository.findByUser(user)
                .map(cart -> {
                    Hibernate.initialize(cart.getCartEntries());
                    return cart;
                })
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    public Cart increaseProductQuantity(Long userId, Long cartId, Long productId, int quantity) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with cart id: " + cartId));
        validateUserAccess(userId, existingCart.getUser().getId());
        Product product = productService.getProductById(productId);
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
        return cartRepository.save(existingCart);
    }


    //carttaki ürün sayısını azaltma
    public Cart decreaseProductQuantity(String username, Long productId, int quantity) {
        Cart existingCart = getOrCreateCart(username);
        Product product = productService.getProductById(productId);
        Optional<CartEntry> existingEntry = existingCart.getCartEntries().stream()
                .filter(entry -> entry.getProduct().equals(product))
                .findFirst();
        if (existingEntry.isPresent()) {
            CartEntry cartEntry = existingEntry.get();
            int newQuantity = cartEntry.getQuantity() - quantity;
            if (newQuantity > 0) {
                cartEntry.setQuantity(cartEntry.getQuantity() - quantity);
            } else {
                existingCart.getCartEntries().remove(cartEntry);
            }
        } else {
            logger.error("Product not found in cart {}", product.getName());
            throw new NotFoundException("Product not found in cart " + product.getName());
        }
        return cartRepository.save(existingCart);
    }

    //kartdan ruunu tamamen silme
    public Cart removeProductFromCart(String username, Long productId) {
        Cart existingCart = getOrCreateCart(username);
        existingCart.getCartEntries().removeIf(entry -> entry.getProduct().getId().equals(productId));
        return cartRepository.save(existingCart);
    }

    //urun sayısı güncellem,productla

    public Cart updateProductQuantity(String username, Long productId, int quantity) {
        Cart existingCart = getOrCreateCart(username);

        for (CartEntry cartEntry : existingCart.getCartEntries()) {
            if (cartEntry.getProduct().getId().equals(productId)) {
                cartEntry.setQuantity(quantity);
                break;
            } else {
                throw new NotFoundException("Product not found in cart entry: " + cartEntry.getProduct().getName());
            }
        }
        return cartRepository.save(existingCart);
    }


    //sepeti tamamen silme
    public void deleteCartByUsername(String username) {
        User user = findUserByUsername(username);
        Cart existingCart = getExistingCart(user);
        if (Objects.isNull(existingCart)) {
            logger.error("Cart not found for username {}", username);
            throw new NotFoundException("Cart not found for username: " + username);
        } else {
            cartRepository.delete(existingCart);
            logger.info("Cart deleted for username {}", username);
        }
    }

    //sepeti silmeden tüm kart entryleri silme
    public void deleteAllCartEntries(String username) {
        User user = findUserByUsername(username);
        Cart existingCart = getExistingCart(user);
        if (Objects.isNull(existingCart)) {
            logger.error("Cart not found for username {}", username);
            throw new NotFoundException("Cart not found for username: " + username);
        } else {
            cartEntryRepository.deleteAll(existingCart.getCartEntries());
            cartRepository.save(existingCart);
            logger.info("Cart entries deleted for username {}", username);
        }
    }

    //product ve quantity syaısını vereke urun ekleme,burda urun sepette olup olmadığı cart entryden anlasılır
    public Cart addProductToCartIfNotPresent(String username, Long productId, int quantity) {
        Cart existingCart = getOrCreateCart(username);
        Product product = productService.getProductById(productId);
        Optional<CartEntry> existingEntry = existingCart.getCartEntries().stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst();
        if (!existingEntry.isPresent()) {
            CartEntry cartEntry = new CartEntry();
            cartEntry.setCart(existingCart);
            cartEntry.setProduct(product);
            cartEntry.setQuantity(quantity);
            existingCart.getCartEntries().add(cartEntry);
            logger.info("Product added to cart: productId={}, username={}", productId, username);
        } else {
            logger.warn("Product already present in cart: productId={}, username={}", productId, username);
        }
        return cartRepository.save(existingCart);
    }


    private User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        logger.info("User found with username {}", username);
        return user;
    }

    private Cart getExistingCart(User user) {
        return cartRepository.findByUser(user).orElse(null);
    }

    private Long getUserIdFromCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return userDetails.getId();
    }

    private void validateUserAccess(Long pathUserId, Long cartUserId) {
        Long currentUserId = getUserIdFromCurrentUser();
        if (!pathUserId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
        if (!cartUserId.equals(pathUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this cart");
        }
    }

}
