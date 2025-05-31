package org.stockify.model.exception;

public class InvalidSessionStatusException extends RuntimeException {
    public InvalidSessionStatusException(String message) {
        super(message);
    }
}
