package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.CartDto;
import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/carts")
public class CartController {

    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}")
    public ResponseEntity<CartDto> getOrCreateCart(@PathVariable Long userId,
                                                   @PathVariable Long cartId) {
        CartDto cartDto = cartService.getOrCreateCart(userId,cartId);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/increase")
    public ResponseEntity<CartDto> increaseProductQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartDto updatedCart = cartService.increaseProductQuantity(userId,cartId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{cartId}/decrease")
    public ResponseEntity<CartDto> decreaseProductQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartDto updatedCart = cartService.decreaseProductQuantity(userId,cartId,productId,quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/remove")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @RequestParam Long productId) {
        cartService.removeProductFromCart(userId,cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}/update")
    public ResponseEntity<CartDto> updateProductQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartDto updatedCart = cartService.updateProductQuantity(userId,cartId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/delete")
    public ResponseEntity<Void> deleteCartByUsername(@PathVariable Long userId,
                                                     @PathVariable Long cartId) {
        cartService.deleteCart(userId,cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/clear-entries")
    public ResponseEntity<Void> deleteAllCartEntries(@PathVariable Long userId,
                                                     @PathVariable Long cartId) {
        cartService.deleteAllCartEntries(userId,cartId);
        return ResponseEntity.noContent().build();
    }
}
