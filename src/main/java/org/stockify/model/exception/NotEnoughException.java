package org.stockify.model.exception;

public class NotEnoughException extends RuntimeException {
    public NotEnoughException(String message) {
        super(message);
    }
}
