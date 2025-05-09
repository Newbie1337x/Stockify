package org.stockify.model.exception;

public class DuplicatedUniqueConstraintException extends RuntimeException {
    public DuplicatedUniqueConstraintException(String message) {
        super(message);
    }
}
