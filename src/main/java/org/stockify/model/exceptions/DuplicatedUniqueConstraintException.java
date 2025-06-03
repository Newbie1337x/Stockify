package org.stockify.model.exceptions;

public class DuplicatedUniqueConstraintException extends RuntimeException {
    public DuplicatedUniqueConstraintException(String message) {
        super(message);
    }
}