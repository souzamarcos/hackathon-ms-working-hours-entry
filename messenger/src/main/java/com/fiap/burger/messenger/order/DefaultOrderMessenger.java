package com.fiap.burger.messenger.order;

import com.fiap.burger.entity.order.Order;
import com.fiap.burger.messenger.adapter.OrderMessenger;
import com.fiap.burger.usecase.misc.profiles.Production;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Production
@Service
public class DefaultOrderMessenger implements OrderMessenger {
    @Value("${cloud.aws.sqs.order-queue}")
    private String queueName;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    public void sendMessage(Order order) {
        var dto = OrderMessageDto.toDto(order);
        this.queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(new Gson().toJson(dto)).build());
    }
}
