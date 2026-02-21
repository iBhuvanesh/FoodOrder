package com.foodorder.order.service;

import com.foodorder.order.client.NotificationClient;
import com.foodorder.order.client.PaymentClient;
import com.foodorder.order.client.RestaurantClient;
import com.foodorder.order.client.dto.NotificationRequest;
import com.foodorder.order.client.dto.PaymentRequest;
import com.foodorder.order.client.dto.PaymentResponse;
import com.foodorder.order.dto.OrderItemRequest;
import com.foodorder.order.dto.PlaceOrderRequest;
import com.foodorder.order.entity.Cart;
import com.foodorder.order.entity.CartItem;
import com.foodorder.order.entity.FoodOrder;
import com.foodorder.order.entity.OrderItem;
import com.foodorder.order.entity.OrderStatus;
import com.foodorder.order.exception.BadRequestException;
import com.foodorder.order.exception.ForbiddenException;
import com.foodorder.order.exception.NotFoundException;
import com.foodorder.order.repository.CartItemRepository;
import com.foodorder.order.repository.CartRepository;
import com.foodorder.order.repository.FoodOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final FoodOrderRepository foodOrderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RestaurantClient restaurantClient;
    private final NotificationClient notificationClient;
    private final PaymentClient paymentClient;

    public OrderService(FoodOrderRepository foodOrderRepository,
                        CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        RestaurantClient restaurantClient,
                        NotificationClient notificationClient,
                        PaymentClient paymentClient) {
        this.foodOrderRepository = foodOrderRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.restaurantClient = restaurantClient;
        this.notificationClient = notificationClient;
        this.paymentClient = paymentClient;
    }

    public FoodOrder placeOrder(PlaceOrderRequest request) {
        validateItems(request.items());

        FoodOrder order = new FoodOrder();
        order.setUserId(request.userId());
        order.setRestaurantId(request.restaurantId());
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.items()) {
            Map<String, Boolean> existsResponse = restaurantClient.menuItemExists(itemRequest.menuItemId());
            if (!existsResponse.getOrDefault("exists", false)) {
                throw new BadRequestException("Invalid menu item: " + itemRequest.menuItemId());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItemId(itemRequest.menuItemId());
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(itemRequest.unitPrice());

            total = total.add(itemRequest.unitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);
        FoodOrder saved = foodOrderRepository.save(order);
        processPaymentOrFail(saved);
        log.info("Order placed: {} by user {}", saved.getId(), saved.getUserId());
        sendOrderNotification(saved.getUserId(), "Order Placed", "Your order #" + saved.getId() + " has been placed successfully.");
        return saved;
    }


    public FoodOrder placeOrderFromCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Long restaurantId = cart.getItems().get(0).getRestaurantId();
        FoodOrder order = new FoodOrder();
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            if (!restaurantId.equals(cartItem.getRestaurantId())) {
                throw new BadRequestException("All cart items must belong to the same restaurant");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItemId(cartItem.getMenuItemId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItems.add(orderItem);

            total = total.add(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        FoodOrder saved = foodOrderRepository.save(order);
        processPaymentOrFail(saved);
        cartItemRepository.deleteAll(cart.getItems());
        log.info("Order placed from cart: {} by user {}", saved.getId(), userId);
        sendOrderNotification(saved.getUserId(), "Order Placed", "Your cart order #" + saved.getId() + " has been placed successfully.");
        return saved;
    }
    public FoodOrder updateStatus(Long orderId, OrderStatus status) {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        order.setStatus(status);
        FoodOrder updated = foodOrderRepository.save(order);
        sendOrderNotification(updated.getUserId(), "Order Status Updated", "Order #" + updated.getId() + " is now " + updated.getStatus());
        return updated;
    }

    public FoodOrder getOrderById(Long orderId) {
        return foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
    }

    public List<FoodOrder> getUserOrders(Long userId) {
        return foodOrderRepository.findByUserId(userId);
    }

    private void processPaymentOrFail(FoodOrder order) {
        PaymentResponse payment = paymentClient.process(new PaymentRequest(order.getId(), order.getUserId(), order.getTotalAmount(), "CARD"));
        if (!"SUCCESS".equals(payment.status())) {
            throw new ForbiddenException("Payment failed for order: " + order.getId());
        }
    }

    private void sendOrderNotification(Long userId, String subject, String message) {
        try {
            notificationClient.sendNotification(new NotificationRequest(userId, "EMAIL", subject, message));
        } catch (Exception ex) {
            log.warn("Failed to send notification for user {}: {}", userId, ex.getMessage());
        }
    }

    private void validateItems(List<OrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new BadRequestException("Order items are required");
        }
    }
}
