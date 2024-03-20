package com.fiap.hackathon.api.dto.workinghour.response;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

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
