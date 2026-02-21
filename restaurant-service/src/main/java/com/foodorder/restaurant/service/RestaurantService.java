package com.foodorder.restaurant.service;

import com.foodorder.restaurant.dto.CreateMenuItemRequest;
import com.foodorder.restaurant.dto.CreateRestaurantRequest;
import com.foodorder.restaurant.entity.MenuItem;
import com.foodorder.restaurant.entity.Restaurant;
import com.foodorder.restaurant.exception.NotFoundException;
import com.foodorder.restaurant.repository.MenuItemRepository;
import com.foodorder.restaurant.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Restaurant createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setCuisine(request.cuisine());
        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant created: {}", saved.getName());
        return saved;
    }

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public MenuItem addMenuItem(Long restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found: " + restaurantId));

        MenuItem item = new MenuItem();
        item.setRestaurant(restaurant);
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());

        MenuItem saved = menuItemRepository.save(item);
        log.info("Menu item created: {} for restaurant {}", saved.getName(), restaurantId);
        return saved;
    }

    public List<MenuItem> getMenuItems(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    public boolean menuItemExists(Long menuItemId) {
        return menuItemRepository.existsById(menuItemId);
    }
}
