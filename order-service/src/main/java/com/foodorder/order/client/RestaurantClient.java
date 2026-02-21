package com.foodorder.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/api/menu/{menuItemId}/exists")
    Map<String, Boolean> menuItemExists(@PathVariable("menuItemId") Long menuItemId);
}
