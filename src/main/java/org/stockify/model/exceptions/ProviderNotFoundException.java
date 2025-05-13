package org.stockify.model.exceptions;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
    public ProviderNotFoundException() {
        super("Provider not found");
    }
}
