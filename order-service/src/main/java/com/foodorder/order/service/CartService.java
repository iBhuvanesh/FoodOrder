package com.foodorder.order.service;

import com.foodorder.order.client.RestaurantClient;
import com.foodorder.order.dto.AddCartItemRequest;
import com.foodorder.order.entity.Cart;
import com.foodorder.order.entity.CartItem;
import com.foodorder.order.exception.BadRequestException;
import com.foodorder.order.exception.NotFoundException;
import com.foodorder.order.repository.CartItemRepository;
import com.foodorder.order.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RestaurantClient restaurantClient;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       RestaurantClient restaurantClient) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.restaurantClient = restaurantClient;
    }

    @Transactional
    public Cart addItem(AddCartItemRequest request) {
        Map<String, Boolean> existsResponse = restaurantClient.menuItemExists(request.menuItemId());
        if (!existsResponse.getOrDefault("exists", false)) {
            throw new BadRequestException("Invalid menu item: " + request.menuItemId());
        }

        Cart cart = cartRepository.findByUserId(request.userId()).orElseGet(() -> {
            Cart c = new Cart();
            c.setUserId(request.userId());
            c.setUpdatedAt(LocalDateTime.now());
            return cartRepository.save(c);
        });

        CartItem item = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), request.menuItemId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setMenuItemId(request.menuItemId());
                    newItem.setRestaurantId(request.restaurantId());
                    newItem.setQuantity(0);
                    newItem.setUnitPrice(request.unitPrice());
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + request.quantity());
        item.setUnitPrice(request.unitPrice());
        cartItemRepository.save(item);

        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.findById(cart.getId()).orElse(cart);
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));
    }

    @Transactional
    public void removeItem(Long userId, Long menuItemId) {
        Cart cart = getCart(userId);
        CartItem item = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElseThrow(() -> new NotFoundException("Item not found in cart"));
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        List<CartItem> items = cart.getItems();
        cartItemRepository.deleteAll(items);
    }
}
