package com.foodorder.order.client;

import com.foodorder.order.client.dto.PaymentRequest;
import com.foodorder.order.client.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse process(@RequestBody PaymentRequest request);
}
