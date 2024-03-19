package com.fiap.hackathon.api.dto.order.response;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WorkingHourRegistryResponseDto(
    @NotNull
    String employeeId,
    @NotNull
    LocalDateTime registryDateTime
) {

    public static WorkingHourRegistryResponseDto toResponseDto(WorkingHourRegistry registry) {
        return new WorkingHourRegistryResponseDto(
            registry.employeeId(),
            registry.registryDateTime()
        );
    }
}
