package com.fiap.hackathon.api.api;

import com.fiap.hackathon.api.dto.workinghour.response.WorkingHourRegistryResponseDto;
import com.fiap.hackathon.controller.adapter.api.WorkingHourRegistryController;
import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.misc.secret.SecretUtils;
import com.fiap.hackathon.api.misc.token.TokenJwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;

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
        var employeeId = "123";
        var authorities = List.of(new SimpleGrantedAuthority("USER"));
        var registry = new WorkingHourRegistry(employeeId, Instant.now());
        var expected = WorkingHourRegistryResponseDto.toResponseDto(registry);

        Authentication authentication = new UsernamePasswordAuthenticationToken(employeeId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(controller.insert(any())).thenReturn(registry);

        WorkingHourRegistryResponseDto actual = api.insert();

        assertEquals(expected, actual);

        verify(controller, times(1)).insert(any());
    }
}
