package com.foodorder.order.service;

import com.foodorder.order.client.PaymentClient;
import com.foodorder.order.client.RestaurantClient;
import com.foodorder.order.dto.OrderItemRequest;
import com.foodorder.order.client.dto.PaymentResponse;
import com.foodorder.order.dto.PlaceOrderRequest;
import com.foodorder.order.entity.FoodOrder;
import com.foodorder.order.repository.CartItemRepository;
import com.foodorder.order.repository.CartRepository;
import com.foodorder.order.repository.FoodOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private FoodOrderRepository foodOrderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private RestaurantClient restaurantClient;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPlaceOrder() {
        when(restaurantClient.menuItemExists(10L)).thenReturn(Map.of("exists", true));
        when(paymentClient.process(any())).thenReturn(new PaymentResponse(1L, 1L, "SUCCESS", "ok"));
        when(foodOrderRepository.save(any(FoodOrder.class))).thenAnswer(i -> i.getArgument(0));

        PlaceOrderRequest request = new PlaceOrderRequest(1L, 2L,
                List.of(new OrderItemRequest(10L, 2, new BigDecimal("100.00"))));

        FoodOrder result = orderService.placeOrder(request);

        Assertions.assertEquals(new BigDecimal("200.00"), result.getTotalAmount());
        Assertions.assertEquals(1, result.getItems().size());
    }
}
