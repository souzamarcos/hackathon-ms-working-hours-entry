package com.fiap.burger.messenger.adapter;

import com.fiap.burger.entity.notification.Notification;

public interface NotificationMessenger {
    void sendMessage(Notification notification);
}
