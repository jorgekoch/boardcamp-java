package com.boardcamp.api.exceptions;

public class ReturnDateConflictException extends RuntimeException {
    public ReturnDateConflictException(String message) {
        super(message);
    }    
}
