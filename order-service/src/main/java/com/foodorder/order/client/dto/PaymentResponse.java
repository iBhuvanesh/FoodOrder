package com.foodorder.order.client.dto;

public record PaymentResponse(Long paymentId, Long orderId, String status, String message) {
}
