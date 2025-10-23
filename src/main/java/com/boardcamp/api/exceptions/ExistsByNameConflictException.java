package com.boardcamp.api.exceptions;

public class ExistsByNameConflictException extends RuntimeException {
    public ExistsByNameConflictException(String message) {
        super(message);
    }
}
