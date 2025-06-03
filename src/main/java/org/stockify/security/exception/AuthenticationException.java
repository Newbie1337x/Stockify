package org.stockify.security.exception;

public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}