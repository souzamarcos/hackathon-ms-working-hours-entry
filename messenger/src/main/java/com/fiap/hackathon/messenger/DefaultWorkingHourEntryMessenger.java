package com.fiap.hackathon.messenger;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.messenger.dto.WorkingHourRegistryMessageDto;
import com.fiap.hackathon.usecase.adapter.messager.WorkingHourEntryMessenger;
import com.fiap.hackathon.usecase.misc.exception.WorkingHourEntryMessagerException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class DefaultWorkingHourEntryMessenger implements WorkingHourEntryMessenger {

    private String queueName;
    private QueueMessagingTemplate queueMessagingTemplate;

    public DefaultWorkingHourEntryMessenger(
        @Value("${cloud.aws.sqs.working-hour-entry-queue}") String queueName,
        QueueMessagingTemplate queueMessagingTemplate
    ) {
        this.queueName = queueName;
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @Override
    public void sendMessage(WorkingHourRegistry registry) {
        try {
            var dto = WorkingHourRegistryMessageDto.toMessageDto(registry);
            queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(new Gson().toJson(dto)).build());
        } catch (MessagingException messagingException) {
            throw new WorkingHourEntryMessagerException(messagingException);
        }
    }
}
