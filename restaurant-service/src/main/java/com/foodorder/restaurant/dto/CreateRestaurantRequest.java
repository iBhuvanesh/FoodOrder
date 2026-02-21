package com.foodorder.restaurant.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRestaurantRequest(
        @NotBlank String name,
        @NotBlank String cuisine
) {
}
