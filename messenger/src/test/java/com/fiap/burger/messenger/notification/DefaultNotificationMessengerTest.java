package com.fiap.burger.messenger.notification;

import com.fiap.burger.entity.notification.Notification;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DefaultNotificationMessengerTest {

    @Mock
    QueueMessagingTemplate queueMessagingTemplate;

    @InjectMocks
    DefaultNotificationMessenger messenger;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendMessage() {
        Order order = new Order(1L, null, 10.0, OrderStatus.AGUARDANDO_PAGAMENTO, LocalDateTime.now(), null, null);
        Notification notification = new Notification(order);
        org.springframework.test.util.ReflectionTestUtils.setField(messenger, "queueName", "notification-queue");

        messenger.sendMessage(notification);
        verify(queueMessagingTemplate, times(1)).send(eq("notification-queue"), any());
    }
}
