package com.fiap.hackathon.messenger.dto;

import com.fiap.hackathon.entity.WorkingHourRegistry;

import java.time.LocalDateTime;

public record  WorkingHourRegistryMessageDto (
    String employeeId,
    LocalDateTime registryDateTime
){
    public static WorkingHourRegistryMessageDto toMessageDto(WorkingHourRegistry registry) {
        return new WorkingHourRegistryMessageDto(registry.employeeId(), registry.registryDateTime());
    }
}
