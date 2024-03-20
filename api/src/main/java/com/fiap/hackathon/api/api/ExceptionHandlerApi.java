package com.fiap.hackathon.api.api;

import com.fiap.hackathon.api.dto.common.ErrorResponseDto;
import com.fiap.hackathon.api.dto.common.ExceptionHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandlerApi {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptions(Exception exception, WebRequest request) {
        LOGGER.error("Handling error: ", exception);
        return new ResponseEntity<>(
            ErrorResponseDto.toErrorResponseDto(exception),
            ExceptionHttpResponse.getHttpStatusBy(exception)
        );
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerApi.class);
}
