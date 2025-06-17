package org.stockify.model.exception;

public class TypeNotAcceptedException extends RuntimeException {
    public TypeNotAcceptedException(String message) {
        super(message);
    }
}
