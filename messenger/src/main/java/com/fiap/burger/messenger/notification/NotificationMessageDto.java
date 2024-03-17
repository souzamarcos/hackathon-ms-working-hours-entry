package com.fiap.burger.messenger.notification;

import com.fiap.burger.entity.notification.Notification;
import com.fiap.burger.entity.notification.NotificationType;

public class NotificationMessageDto{
    private String customerId;
    private Long orderId;
    private NotificationType notificationType;

    public NotificationMessageDto(Notification notification) {
        this.orderId = notification.getOrderId();
        this.customerId = notification.getCustomerId();
        this.notificationType = notification.getNotificationType();
    }

}
