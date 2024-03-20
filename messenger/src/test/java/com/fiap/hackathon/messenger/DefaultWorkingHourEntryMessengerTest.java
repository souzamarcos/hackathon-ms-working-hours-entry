package com.fiap.hackathon.messenger;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.misc.exception.WorkingHourEntryMessagerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class DefaultWorkingHourEntryMessengerTest {

    private String queueName = "queue-name";

    @Mock
    private QueueMessagingTemplate queueMessagingTemplate;

    private DefaultWorkingHourEntryMessenger messenger;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        messenger = new DefaultWorkingHourEntryMessenger(queueName, queueMessagingTemplate);


    }

    @Test
    void shouldSendWorkingHourEntryMessage() {
        var employeeId = "123";
        var registry = new WorkingHourRegistry(employeeId, Instant.now());

        doNothing().when(queueMessagingTemplate).send(eq(queueName), any());

        assertDoesNotThrow(() -> messenger.sendMessage(registry));

        verify(queueMessagingTemplate, times(1)).send(eq(queueName),any());
    }

    @Test
    void shouldThrowWorkingHourEntryMessagerException() {
        var employeeId = "123";
        var registry = new WorkingHourRegistry(employeeId, Instant.now());

        doThrow(new WorkingHourEntryMessagerException()).when(queueMessagingTemplate).send(eq(queueName), any());

        assertThrows(WorkingHourEntryMessagerException.class, () -> messenger.sendMessage(registry));

        verify(queueMessagingTemplate, times(1)).send(eq(queueName),any());
    }
}