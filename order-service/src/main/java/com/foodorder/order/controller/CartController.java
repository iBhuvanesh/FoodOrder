package com.foodorder.order.controller;

import com.foodorder.order.dto.AddCartItemRequest;
import com.foodorder.order.entity.Cart;
import com.foodorder.order.exception.ForbiddenException;
import com.foodorder.order.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public Cart addItem(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                        @Valid @RequestBody AddCartItemRequest request) {
        ensureUserOrAdmin(role);
        return cartService.addItem(request);
    }

    @GetMapping("/{userId}")
    public Cart getCart(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                        @PathVariable Long userId) {
        ensureUserOrAdmin(role);
        return cartService.getCart(userId);
    }

    @DeleteMapping("/{userId}/items/{menuItemId}")
    public ResponseEntity<Void> removeItem(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                           @PathVariable Long userId,
                                           @PathVariable Long menuItemId) {
        ensureUserOrAdmin(role);
        cartService.removeItem(userId, menuItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clear(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                      @PathVariable Long userId) {
        ensureUserOrAdmin(role);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    private void ensureUserOrAdmin(String role) {
        if (!"USER".equals(role) && !"RESTAURANT_ADMIN".equals(role)) {
            throw new ForbiddenException("Only USER or RESTAURANT_ADMIN can access cart APIs");
        }
    }
}
