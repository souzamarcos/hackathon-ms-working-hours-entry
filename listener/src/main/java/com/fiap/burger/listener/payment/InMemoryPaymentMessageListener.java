package com.fiap.burger.listener.payment;

import com.fiap.burger.controller.adapter.api.OrderController;
import com.fiap.burger.entity.order.OrderPaymentStatus;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.usecase.misc.exception.PaymentMessageListenerException;
import com.fiap.burger.usecase.misc.profiles.Test;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Test
@Component
public class InMemoryPaymentMessageListener {
    @Autowired
    OrderController orderController;

    public void paymentQueueListener(String message) {
        try {
            var dto = new Gson().fromJson(message, PaymentMessageListenerDto.class);
            if(dto.getOrderPaymentStatus().equals(OrderPaymentStatus.APROVADO)) {
                orderController.checkout(dto.getOrderId());
            } else if(dto.getOrderPaymentStatus().equals(OrderPaymentStatus.RECUSADO)) {
                orderController.updateStatus(dto.getOrderId(), OrderStatus.CANCELADO);
            }
        } catch (Exception ex) {
            throw new PaymentMessageListenerException(ex.getMessage());
        }
    }
}
