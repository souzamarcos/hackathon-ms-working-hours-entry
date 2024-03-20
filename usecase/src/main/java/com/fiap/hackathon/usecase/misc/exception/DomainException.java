package com.fiap.hackathon.usecase.misc.exception;

import java.util.Map;

public abstract class DomainException extends RuntimeException {

    private final Map<String, String> parameters;

    protected DomainException(String message) {
        super(message);
        this.parameters = Map.of();
    }

    protected DomainException(String message,Throwable cause) {
        super(message, cause);
        this.parameters = Map.of();
    }

    protected DomainException(String message, Map<String, String> parameters) {
        super(message);
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

}
