package org.stockify.model.exceptions;

public class ClientNotFoundException extends NotFoundException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}