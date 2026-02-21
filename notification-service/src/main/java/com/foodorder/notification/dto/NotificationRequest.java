package com.foodorder.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotNull Long userId,
        @NotBlank String channel,
        @NotBlank String subject,
        @NotBlank String message
) {
}
