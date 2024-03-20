package com.fiap.hackathon.usecase.misc.exception;

import java.util.Map;

public class NegativeOrZeroValueException extends DomainException {

    public NegativeOrZeroValueException(String attribute) {
        super("Attribute `" + attribute + "` can not be zero or less.", Map.of("attribute", attribute));
    }
}
