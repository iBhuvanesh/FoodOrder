package com.foodorder.order.client.dto;

import java.math.BigDecimal;

public record PaymentRequest(Long orderId, Long userId, BigDecimal amount, String method) {
}
