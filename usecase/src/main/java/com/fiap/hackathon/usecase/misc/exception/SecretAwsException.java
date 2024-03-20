package com.fiap.hackathon.usecase.misc.exception;

public class SecretAwsException extends DomainException {

    public SecretAwsException(String message) {
        super(message);
    }
    public SecretAwsException(String message, Throwable cause) {
        super(message, cause);
    }

}

