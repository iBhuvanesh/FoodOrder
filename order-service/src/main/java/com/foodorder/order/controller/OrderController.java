package com.foodorder.order.controller;

import com.foodorder.order.dto.PlaceOrderRequest;
import com.foodorder.order.entity.FoodOrder;
import com.foodorder.order.entity.OrderStatus;
import com.foodorder.order.exception.ForbiddenException;
import com.foodorder.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public FoodOrder placeOrder(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                @Valid @RequestBody PlaceOrderRequest request) {
        ensureUserOrAdmin(role);
        return orderService.placeOrder(request);
    }


    @PostMapping("/from-cart/{userId}")
    public FoodOrder placeFromCart(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                   @PathVariable Long userId) {
        ensureUserOrAdmin(role);
        return orderService.placeOrderFromCart(userId);
    }
    @PatchMapping("/{orderId}/status")
    public FoodOrder updateStatus(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                  @PathVariable Long orderId,
                                  @RequestParam OrderStatus status) {
        if (!"RESTAURANT_ADMIN".equals(role)) {
            throw new ForbiddenException("Only RESTAURANT_ADMIN can update order status");
        }
        return orderService.updateStatus(orderId, status);
    }

    @GetMapping("/{orderId}")
    public FoodOrder getById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<FoodOrder> getByUser(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                     @PathVariable Long userId) {
        ensureUserOrAdmin(role);
        return orderService.getUserOrders(userId);
    }

    private void ensureUserOrAdmin(String role) {
        if (!"USER".equals(role) && !"RESTAURANT_ADMIN".equals(role)) {
            throw new ForbiddenException("Only USER or RESTAURANT_ADMIN can access order APIs");
        }
    }
}
