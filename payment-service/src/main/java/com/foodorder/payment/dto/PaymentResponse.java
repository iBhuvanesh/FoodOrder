package com.foodorder.payment.dto;

public record PaymentResponse(Long paymentId, Long orderId, String status, String message) {
}
