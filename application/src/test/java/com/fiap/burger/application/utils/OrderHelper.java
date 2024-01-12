package com.fiap.burger.application.utils;

import com.fiap.burger.api.dto.order.request.OrderInsertRequestDto;
import com.fiap.burger.api.dto.order.request.OrderItemInsertRequestDto;
import com.fiap.burger.api.dto.order.request.OrderUpdateStatusRequestDto;
import com.fiap.burger.entity.order.OrderPaymentStatus;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.listener.payment.PaymentMessageListenerDto;

import java.util.Arrays;

public class OrderHelper {

    public static OrderInsertRequestDto createOrderRequest() {
        var item = new OrderItemInsertRequestDto(2L, Arrays.asList(20L), "Comentário");

        return new OrderInsertRequestDto(null, Arrays.asList(item));
    }

    public static OrderUpdateStatusRequestDto createUpdateStatusRequest(OrderStatus orderStatus) {
        return new OrderUpdateStatusRequestDto(orderStatus);
    }

    public static PaymentMessageListenerDto createPaymentMessageListenerDto(Long orderId, OrderPaymentStatus paymentStatus) {
        var genericPaymentId = 1L;
        return new PaymentMessageListenerDto(genericPaymentId, orderId, paymentStatus);
    }

}
