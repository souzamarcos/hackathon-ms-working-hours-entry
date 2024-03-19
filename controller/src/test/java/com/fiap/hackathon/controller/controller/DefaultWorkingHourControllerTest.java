package com.fiap.hackathon.controller.controller;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.adapter.usecase.WorkingHourUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultWorkingHourControllerTest {

    @Mock
    WorkingHourUseCase useCase;

    @InjectMocks
    DefaultWorkingHourRegistryRegistryController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class insert {
        @Test
        void shouldInsert() {
            var expected = new WorkingHourRegistry("123", LocalDateTime.now());

            when(useCase.insert(expected)).thenReturn(expected);

            WorkingHourRegistry actual = controller.insert(expected);

            assertEquals(expected, actual);

            verify(useCase, times(1)).insert(expected);
        }
    }
}
