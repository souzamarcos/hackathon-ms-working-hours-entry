package com.fiap.hackathon.usecase.misc.exception;

import java.util.Map;

public class InvalidAttributeException extends DomainException {

    public InvalidAttributeException(String message, String attribute) {
        super(message, Map.of("attribute", attribute));
    }
}
