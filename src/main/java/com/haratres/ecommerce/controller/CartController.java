package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.model.Cart;
import com.haratres.ecommerce.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/carts")//userid yi id ye cevri
public class CartController {

    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    // Sepeti ve ürünleri almak veya oluşturmak için
    @PostMapping() //sadece postmapping olsun bu neden? ,usernamelerin hepsi userid olarak degiğşcek,getorcreatecartb mantıklı olmıs
    public ResponseEntity<Cart> getOrCreateCart(@PathVariable String username) {
        Cart cart = cartService.getOrCreateCart(username);
        return ResponseEntity.ok(cart);
    }

    // Sepete ürün ekleme
    @PostMapping("/{cartId}/increase")
    public ResponseEntity<Cart> increaseProductQuantity(
            @PathVariable String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.increaseProductQuantity(username, productId, quantity); //cartidyi kullan usera fian gerek yok
        //ana pathte gonderilen userid cartiddeki cartla eslesiyor mu
        //security cpntexten kullancıyı cek o kullanıcının cureentuser la os istekle o karttaki user eşleşiyor mu
        //hasrole hasauthority yi kulanabilirisn any authoritu
        return ResponseEntity.ok(updatedCart);
    }

    // Sepetteki ürün miktarını azaltma
    @PostMapping("/{username}/decrease")
    public ResponseEntity<Cart> decreaseProductQuantity(
            @PathVariable String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.decreaseProductQuantity(username, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Sepetten ürün kaldırma
    @DeleteMapping("/{username}/remove")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable String username,
            @RequestParam Long productId) {
        cartService.removeProductFromCart(username, productId);
        return ResponseEntity.noContent().build();
    }

    // Sepetteki ürün miktarını güncelleme
    @PostMapping("/{username}/update")
    public ResponseEntity<Cart> updateProductQuantity(
            @PathVariable String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.updateProductQuantity(username, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Sepeti tamamen silme
    @DeleteMapping("/{username}/delete")
    public ResponseEntity<Void> deleteCartByUsername(@PathVariable String username) {
        cartService.deleteCartByUsername(username);
        return ResponseEntity.noContent().build();
    }

    // Sepeti silmeden tüm kart girişlerini silme
    @DeleteMapping("/{username}/clear-entries")
    public ResponseEntity<Void> deleteAllCartEntries(@PathVariable String username) {
        cartService.deleteAllCartEntries(username);
        //butun cartentry cekip java tarafında kontrole etme
        //native query ile repository de elle sorgu yazabiliriz jpanın yetmediği yerde

        return ResponseEntity.noContent().build();
    }

    // Ürün sepette yoksa yeni bir giriş ekleme
    @PostMapping("/{username}/add-if-not-present")
    public ResponseEntity<Cart> addProductToCartIfNotPresent(
            @PathVariable String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.addProductToCartIfNotPresent(username, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }
}
