package com.foodorder.order.client.dto;

public record NotificationRequest(Long userId, String channel, String subject, String message) {
}
