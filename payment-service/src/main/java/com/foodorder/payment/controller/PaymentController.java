package com.foodorder.payment.controller;

import com.foodorder.payment.dto.PaymentRequest;
import com.foodorder.payment.dto.PaymentResponse;
import com.foodorder.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse process(@Valid @RequestBody PaymentRequest request) {
        return paymentService.process(request);
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse getByOrder(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }
}
