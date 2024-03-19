package com.fiap.hackathon.usecase.misc.exception;

public class WorkingHourEntryMessagerException extends DomainException {
    public WorkingHourEntryMessagerException(Throwable exception) {
        super("An exception was thrown while sending messaging to Order Queue", exception);
    }
}
