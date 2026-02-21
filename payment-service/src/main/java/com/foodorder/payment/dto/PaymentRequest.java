package com.foodorder.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long orderId,
        @NotNull Long userId,
        @NotNull @DecimalMin("0.1") BigDecimal amount,
        @NotBlank String method
) {
}
