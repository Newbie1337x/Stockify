package org.stockify.model.exception;

public class InvalidSortParameterException extends RuntimeException {
    public InvalidSortParameterException(String message) {
        super(message);
    }
}
