package com.fiap.hackathon.usecase.misc.exception;

import java.util.Map;

public class EmptyAttributeException extends DomainException {

    public EmptyAttributeException(String attribute) {
        super("Attribute `" + attribute + "` can not be empty.", Map.of("attribute", attribute));
    }
}
