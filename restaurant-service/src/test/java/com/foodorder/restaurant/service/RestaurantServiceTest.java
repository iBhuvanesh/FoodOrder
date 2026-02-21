package com.foodorder.restaurant.service;

import com.foodorder.restaurant.dto.CreateRestaurantRequest;
import com.foodorder.restaurant.entity.Restaurant;
import com.foodorder.restaurant.repository.MenuItemRepository;
import com.foodorder.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void shouldCreateRestaurant() {
        Restaurant saved = new Restaurant();
        saved.setId(1L);
        saved.setName("Taste Hub");
        saved.setCuisine("Indian");

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);

        Restaurant result = restaurantService.createRestaurant(new CreateRestaurantRequest("Taste Hub", "Indian"));

        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("Taste Hub", result.getName());
    }
}
