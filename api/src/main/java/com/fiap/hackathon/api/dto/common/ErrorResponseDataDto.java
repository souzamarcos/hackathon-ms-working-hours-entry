package com.fiap.hackathon.api.dto.common;

import com.fiap.hackathon.usecase.misc.exception.DomainException;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ErrorResponseDataDto(
    @NotNull String message,
    @NotNull Map<String, String> parameters
) {
    public static ErrorResponseDataDto toErrorResponseDataDto(DomainException exception) {
        return new ErrorResponseDataDto(exception.getMessage(), exception.getParameters());
    }
}
