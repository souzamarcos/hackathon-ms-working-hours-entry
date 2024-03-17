package com.fiap.burger.usecase.misc.exception;

public class NotificationMessagerException extends DomainException {
    public NotificationMessagerException(Throwable exception) {
        super("An exception was thrown while sending messaging to Notification Queue", exception);
    }
}
