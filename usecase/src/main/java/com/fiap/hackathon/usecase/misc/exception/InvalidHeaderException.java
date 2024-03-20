package com.fiap.hackathon.usecase.misc.exception;

import java.util.Map;

public class InvalidHeaderException extends DomainException {

    public InvalidHeaderException(String message, String header) {
        super(message, Map.of("header", header));
    }
}
