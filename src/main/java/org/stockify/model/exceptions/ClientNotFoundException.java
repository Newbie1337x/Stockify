package org.stockify.model.exceptions;

import java.util.NoSuchElementException;

public class ClientNotFoundException extends NoSuchElementException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
