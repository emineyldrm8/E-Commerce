package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.CartDto;
import com.haratres.ecommerce.service.CartEntryService;
import com.haratres.ecommerce.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/carts")
public class UserCartController {

    private final Logger logger = LoggerFactory.getLogger(UserCartController.class);

    private final CartService cartService;
    private final CartEntryService cartEntryService;

    public UserCartController(CartService cartService, CartEntryService cartEntryService) {
        this.cartService = cartService;
        this.cartEntryService = cartEntryService;
    }

    @PostMapping()
    public ResponseEntity<CartDto> getOrCreateCart(@PathVariable Long userId) {
        CartDto cartDto = cartService.getOrCreateCart(userId);
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

    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @PathVariable Long productId) {
        cartService.removeProductFromCart(userId,cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartDto> updateProductQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        CartDto updatedCart = cartService.updateProductQuantity(userId,cartId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCartByUsername(@PathVariable Long userId,
                                                     @PathVariable Long cartId) {
        cartService.deleteCart(userId,cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/entries")
    public ResponseEntity<Void> deleteAllCartEntries(@PathVariable Long userId,
                                                     @PathVariable Long cartId) {
        cartService.deleteAllCartEntries(userId,cartId);
        return ResponseEntity.noContent().build();
    }
}
