package com.fiap.burger.listener.payment;

import com.fiap.burger.entity.order.OrderPaymentStatus;

public class PaymentMessageListenerDto {
    private Long id;
    private Long orderId;
    private OrderPaymentStatus status;

    public PaymentMessageListenerDto(Long id, Long orderId, OrderPaymentStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderPaymentStatus getOrderPaymentStatus() {
        return status;
    }
}
