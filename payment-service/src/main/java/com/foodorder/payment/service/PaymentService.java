package com.foodorder.payment.service;

import com.foodorder.payment.dto.PaymentRequest;
import com.foodorder.payment.dto.PaymentResponse;
import com.foodorder.payment.entity.Payment;
import com.foodorder.payment.entity.PaymentStatus;
import com.foodorder.payment.exception.NotFoundException;
import com.foodorder.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse process(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.orderId());
        payment.setUserId(request.userId());
        payment.setAmount(request.amount());
        payment.setMethod(request.method());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(simulatePayment());

        Payment saved = paymentRepository.save(payment);
        log.info("Payment processed for order {} with status {}", request.orderId(), saved.getStatus());

        return new PaymentResponse(
                saved.getId(),
                saved.getOrderId(),
                saved.getStatus().name(),
                saved.getStatus() == PaymentStatus.SUCCESS ? "Payment successful" : "Payment failed"
        );
    }

    public PaymentResponse getByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found for order: " + orderId));

        return new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getStatus().name(), "Fetched payment status");
    }

    private PaymentStatus simulatePayment() {
        return new Random().nextInt(10) < 8 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }
}
