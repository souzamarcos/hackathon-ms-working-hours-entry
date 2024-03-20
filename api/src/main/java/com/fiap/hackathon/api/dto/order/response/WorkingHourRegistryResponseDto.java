package com.fiap.hackathon.api.dto.order.response;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record WorkingHourRegistryResponseDto(
    @NotNull
    String employeeId,
    @NotNull
    Instant registryDateTime
) {

    public static WorkingHourRegistryResponseDto toResponseDto(WorkingHourRegistry registry) {
        return new WorkingHourRegistryResponseDto(
            registry.employeeId(),
            registry.registryDateTime()
        );
    }
}
