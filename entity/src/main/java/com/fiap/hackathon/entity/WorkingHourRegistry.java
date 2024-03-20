package com.fiap.hackathon.entity;

import java.time.Instant;

public record WorkingHourRegistry (
    String employeeId,
    Instant registryDateTime
) {}
