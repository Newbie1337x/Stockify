package org.stockify.model.exception;

public class ClientNotFoundException extends NotFoundException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}