package com.boardcamp.api.exceptions;

public class CustomersIdConflictException extends RuntimeException {
    public CustomersIdConflictException(String message) {
        super(message);
    }
}
