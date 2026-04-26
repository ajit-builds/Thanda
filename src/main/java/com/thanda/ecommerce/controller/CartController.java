package com.thanda.ecommerce.controller;

import com.thanda.ecommerce.dto.CartRequest;
import com.thanda.ecommerce.entity.CartItem;
import com.thanda.ecommerce.entity.User;
import com.thanda.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.getCart(user.getId()));
    }

    @PostMapping
    public ResponseEntity<CartItem> add(@AuthenticationPrincipal User user, @RequestBody CartRequest req) {
        return ResponseEntity.ok(service.addToCart(user.getId(), req.getProductId(), req.getQuantity()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        service.removeFromCart(id);
        return ResponseEntity.ok().build();
    }
}
