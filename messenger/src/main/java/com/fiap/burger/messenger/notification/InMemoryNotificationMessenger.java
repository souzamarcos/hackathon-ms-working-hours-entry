package com.fiap.burger.messenger.notification;

import com.fiap.burger.entity.notification.Notification;
import com.fiap.burger.messenger.adapter.NotificationMessenger;
import com.fiap.burger.usecase.misc.profiles.Test;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import static java.lang.System.*;

@Test
@Primary
@Service
public class InMemoryNotificationMessenger implements NotificationMessenger {
    public void sendMessage(Notification notification) {
        out.println("Notification for customer " + notification.getCustomerId() +
            ", order " + notification.getOrderId() +
            " and type " + notification.getNotificationType() + " was sent through messenger");
    }
}
