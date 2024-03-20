package com.fiap.hackathon.usecase.misc.exception;

public class TokenJwtException extends DomainException {

    public TokenJwtException(String message) {
        super(message);
    }

    public TokenJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}

