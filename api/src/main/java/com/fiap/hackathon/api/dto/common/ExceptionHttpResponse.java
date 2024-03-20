package com.fiap.hackathon.api.dto.common;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fiap.hackathon.usecase.misc.exception.DomainException;
import com.fiap.hackathon.usecase.misc.exception.NotFoundException;
import org.springframework.http.HttpStatus;

public class ExceptionHttpResponse {
    private ExceptionHttpResponse() {
        throw new IllegalStateException("Utility class");
    }
    public static HttpStatus getHttpStatusBy(Exception exception) {
        if (exception instanceof NotFoundException) return HttpStatus.NOT_FOUND;
        if (exception instanceof JWTVerificationException) return HttpStatus.UNAUTHORIZED;
        if (exception instanceof DomainException) return HttpStatus.BAD_REQUEST;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
