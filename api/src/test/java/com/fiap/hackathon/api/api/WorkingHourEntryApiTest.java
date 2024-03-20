package com.fiap.hackathon.api.api;

import com.fiap.hackathon.api.dto.order.response.WorkingHourRegistryResponseDto;
import com.fiap.hackathon.controller.adapter.api.WorkingHourRegistryController;
import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.misc.secret.SecretUtils;
import com.fiap.hackathon.usecase.misc.token.TokenJwtSecret;
import com.fiap.hackathon.usecase.misc.token.TokenJwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkingHourEntryApiTest {

    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";

    @Mock
    SecretUtils secretUtils;
    @Mock
    WorkingHourRegistryController controller;

    WorkingHourEntryApi api;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        api = new WorkingHourEntryApi(new TokenJwtUtils(secretUtils), controller);
    }
    @Test
    void shouldInsertWorkingHourEntry() {
        var headerToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImlzcyI6IlRFU1QtSVNTVUVSIiwiaWQiOiIxMjMiLCJuYW1lIjoiVGVzdGUiLCJlbWFpbCI6InRlc3RlQHRlc3RlLmNvbSIsInR5cGUiOiJVU0VSIiwiaWF0IjoxNzA2NDEzNDMyLCJqdGkiOiJmMDQyM2U0Yy03YzEwLTRiMzUtOGQ5NS1kYmZhZmMzY2ZkYTQifQ.mPm-fgLutJWpOCdCTAMJcS1XIHFclHD6kM3qOMEaJUw";
        var employeeId = "123";
        var registry = new WorkingHourRegistry(employeeId, Instant.now());
        var expected = WorkingHourRegistryResponseDto.toResponseDto(registry);

        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));
        when(controller.insert(any())).thenReturn(registry);

        WorkingHourRegistryResponseDto actual = api.insert(headerToken);

        assertEquals(expected, actual);

        verify(controller, times(1)).insert(any());
    }
}
