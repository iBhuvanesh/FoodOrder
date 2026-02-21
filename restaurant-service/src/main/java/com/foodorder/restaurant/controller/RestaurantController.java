package com.foodorder.restaurant.controller;

import com.foodorder.restaurant.dto.CreateMenuItemRequest;
import com.foodorder.restaurant.dto.CreateRestaurantRequest;
import com.foodorder.restaurant.entity.MenuItem;
import com.foodorder.restaurant.entity.Restaurant;
import com.foodorder.restaurant.exception.ForbiddenException;
import com.foodorder.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/restaurants")
    public Restaurant createRestaurant(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                       @Valid @RequestBody CreateRestaurantRequest request) {
        ensureAdmin(role);
        return restaurantService.createRestaurant(request);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    @PostMapping("/restaurants/{restaurantId}/menu")
    public MenuItem addMenuItem(@RequestHeader(value = "X-Auth-Role", required = false) String role,
                                @PathVariable Long restaurantId,
                                @Valid @RequestBody CreateMenuItemRequest request) {
        ensureAdmin(role);
        return restaurantService.addMenuItem(restaurantId, request);
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    public List<MenuItem> getMenuItems(@PathVariable Long restaurantId) {
        return restaurantService.getMenuItems(restaurantId);
    }

    @GetMapping("/menu/{menuItemId}/exists")
    public Map<String, Boolean> menuItemExists(@PathVariable Long menuItemId) {
        return Map.of("exists", restaurantService.menuItemExists(menuItemId));
    }

    private void ensureAdmin(String role) {
        if (!"RESTAURANT_ADMIN".equals(role)) {
            throw new ForbiddenException("Only RESTAURANT_ADMIN can modify restaurant/menu data");
        }
    }
}
