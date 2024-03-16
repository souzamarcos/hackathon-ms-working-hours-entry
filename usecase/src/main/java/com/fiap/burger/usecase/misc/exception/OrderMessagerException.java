package com.fiap.burger.usecase.misc.exception;

public class OrderMessagerException extends DomainException {
    public OrderMessagerException(Throwable exception) {
        super("An exception was thrown while sending messaging to Order Queue", exception);
    }
}
