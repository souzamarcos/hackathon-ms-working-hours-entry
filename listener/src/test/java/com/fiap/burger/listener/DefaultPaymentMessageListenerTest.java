package com.fiap.burger.listener;

import com.fiap.burger.controller.adapter.api.OrderController;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.listener.payment.DefaultPaymentMessageListener;
import com.fiap.burger.usecase.misc.exception.PaymentMessageListenerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultPaymentMessageListenerTest {
    @InjectMocks
    DefaultPaymentMessageListener listener;

    @Mock
    OrderController controller;

    @Test
    void shouldCheckoutOrder() {
        when(controller.checkout(1L)).thenReturn(new Order(1L, null, 10.0, OrderStatus.RECEBIDO, LocalDateTime.now(), null, null));

        listener.paymentQueueListener("{\"id\":1,\"orderId\":1,\"status\":\"APROVADO\"}");

        verify(controller, times(1)).checkout(1L);
        verify(controller, never()).updateStatus(anyLong(), any());
    }

    @Test
    void shouldCancelOrder() {
        when(controller.updateStatus(1L, OrderStatus.CANCELADO)).thenReturn(new Order(1L, null, 10.0, OrderStatus.CANCELADO, LocalDateTime.now(), null, null));

        listener.paymentQueueListener("{\"id\":1,\"orderId\":1,\"status\":\"RECUSADO\"}");

        verify(controller, never()).checkout(anyLong());
        verify(controller, times(1)).updateStatus(1L, OrderStatus.CANCELADO);
    }

    @Test
    void shouldThrownOrderMessageListenerExceptionWhenExceptionInInserting() {
        var expected = new PaymentMessageListenerException("Mock Error");
        when(controller.checkout(1L)).thenThrow(new RuntimeException("Mock Error"));

        PaymentMessageListenerException actual = assertThrows(PaymentMessageListenerException.class, () ->
            listener.paymentQueueListener("{\"id\":1,\"orderId\":1,\"status\":\"APROVADO\"}"));

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(controller, times(1)).checkout(1L);
        verify(controller, never()).updateStatus(anyLong(), any());
    }
}
