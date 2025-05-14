package org.stockify.model.exception;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
    public ProviderNotFoundException() {
        super("Provider not found");
    }
}
