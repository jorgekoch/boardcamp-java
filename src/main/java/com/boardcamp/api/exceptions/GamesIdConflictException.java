package com.boardcamp.api.exceptions;

public class GamesIdConflictException extends RuntimeException {
    public GamesIdConflictException(String message) {
        super(message);
    }    
}
