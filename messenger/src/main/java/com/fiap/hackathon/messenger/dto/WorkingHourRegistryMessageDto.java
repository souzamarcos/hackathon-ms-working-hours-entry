package com.fiap.hackathon.messenger.dto;

import com.fiap.hackathon.entity.WorkingHourRegistry;

import java.time.Instant;

public record  WorkingHourRegistryMessageDto (
    String employeeId,
    Instant registryDateTime
){
    public static WorkingHourRegistryMessageDto toMessageDto(WorkingHourRegistry registry) {
        return new WorkingHourRegistryMessageDto(registry.employeeId(), registry.registryDateTime());
    }
}
