package com.fiap.hackathon.api.dto.common;

import com.fiap.hackathon.usecase.misc.exception.DomainException;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record ErrorResponseDto(@NotNull List<ErrorResponseDataDto> errors) {
    public static ErrorResponseDto toErrorResponseDto(Exception exception) {
        if (exception instanceof DomainException domainException) {
            return new ErrorResponseDto(
                List.of(new ErrorResponseDataDto(exception.getMessage(), domainException.getParameters()))
            );
        } else return defaultError();
    }

    private static ErrorResponseDto defaultError() {
        return new ErrorResponseDto(
            List.of(new ErrorResponseDataDto("Unexpected error", Map.of()))
        );
    }
}
