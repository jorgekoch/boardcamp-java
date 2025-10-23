package com.boardcamp.api.exceptions;

public class ExistsByCpfConflictException extends RuntimeException {
    public ExistsByCpfConflictException(String message) {
        super(message);
    }
}
