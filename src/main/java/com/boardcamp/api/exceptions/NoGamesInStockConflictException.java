package com.boardcamp.api.exceptions;

public class NoGamesInStockConflictException extends RuntimeException {
    public NoGamesInStockConflictException(String message) {
        super(message);
    }
    
}
