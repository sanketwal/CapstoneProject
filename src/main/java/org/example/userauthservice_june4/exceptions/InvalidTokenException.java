package org.example.userauthservice_june4.exceptions;

public class InvalidTokenException extends RuntimeException {
    private String message;

    public InvalidTokenException(String message) {
        super(message);
    }
}
