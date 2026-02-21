package com.foodorder.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddCartItemRequest(
        @NotNull Long userId,
        @NotNull Long restaurantId,
        @NotNull Long menuItemId,
        @NotNull @Min(1) Integer quantity,
        @NotNull @DecimalMin("0.1") BigDecimal unitPrice
) {
}
