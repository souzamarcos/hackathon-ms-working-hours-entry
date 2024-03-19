package com.fiap.hackathon.entity;

import java.time.LocalDateTime;

public record WorkingHourRegistry (
    String employeeId,
    LocalDateTime registryDateTime
) {}
