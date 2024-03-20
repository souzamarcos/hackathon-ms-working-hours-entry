package com.fiap.hackathon.usecase.misc.exception;

import java.util.Map;

public class NullAttributeException extends DomainException {

    public NullAttributeException(String attribute) {
        super("Attribute `" + attribute + "` can not be null.", Map.of("attribute", attribute));
    }
}
