package com.fiap.hackathon.usecase.usecase;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.adapter.messager.WorkingHourEntryMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultOrderUseCaseTest {

    @Mock
    WorkingHourEntryMessenger messenger;

    @InjectMocks
    DefaultWorkingHourUseCase useCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldInsert() {
        var registry = new WorkingHourRegistry("123", LocalDateTime.now());
        var expected = registry;

        doNothing().when(messenger).sendMessage(registry);

        var actual = useCase.insert(registry);

        assertEquals(expected, actual);

        verify(messenger, times(1)).sendMessage(registry);
    }
}
