package com.fiap.burger.usecase.misc.exception;

public class PaymentMessageListenerException extends DomainException {
    public PaymentMessageListenerException(String message) {
        super("An exception was thrown during the execution of the SQS listener of Payment Queue and Message will be still available in Queue. Error: " + message);
    }
}
