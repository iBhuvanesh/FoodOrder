package com.foodorder.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PlaceOrderRequest(
        @NotNull Long userId,
        @NotNull Long restaurantId,
        @NotEmpty List<@Valid OrderItemRequest> items
) {
}
