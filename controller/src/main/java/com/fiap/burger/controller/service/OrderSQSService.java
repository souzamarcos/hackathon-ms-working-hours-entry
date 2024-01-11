package com.fiap.burger.controller.service;

import com.fiap.burger.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderSQSService {

    private static final String QUEUE_NAME = "order-queue";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    public void sendMessage(Order order) {
        this.queueMessagingTemplate.send(QUEUE_NAME, MessageBuilder.withPayload(order.toString()).build());
    }

}
