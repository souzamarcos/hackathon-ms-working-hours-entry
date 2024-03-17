package com.fiap.burger.messenger.notification;

import com.fiap.burger.entity.notification.Notification;
import com.fiap.burger.messenger.adapter.NotificationMessenger;
import com.fiap.burger.usecase.misc.exception.NotificationMessagerException;
import com.fiap.burger.usecase.misc.profiles.NotTest;
import com.fiap.burger.usecase.misc.profiles.Production;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Production
@NotTest
@Service
public class DefaultNotificationMessenger implements NotificationMessenger {
    @Value("${cloud.aws.sqs.notification-queue}")
    private String queueName;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Override
    public void sendMessage(Notification notification) {
        try {
            var dto = new NotificationMessageDto(notification);
            this.queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(new Gson().toJson(dto)).build());
        } catch (MessagingException messagingException) {
            throw new NotificationMessagerException(messagingException);
        }
    }
}
