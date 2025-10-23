package com.boardcamp.api.exceptions;

public class RentalsIdConflictException extends RuntimeException {
    public RentalsIdConflictException(String message) {
        super(message);
    }    
}
