package com.boardcamp.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({GamesIdConflictException.class})
    public ResponseEntity<String> handleCustomersIdConflictException(CustomersIdConflictException exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler({CustomersIdConflictException.class})
    public ResponseEntity<String> handleGamesIdConflictException(GamesIdConflictException exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler({RentalsIdConflictException.class})
    public ResponseEntity<String> handleRentalsIdConflictException(RentalsIdConflictException exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler({ReturnDateConflictException.class})
    public ResponseEntity<String> handleReturnDateConflictException(ReturnDateConflictException exception) {
        return ResponseEntity.status(400).body(exception.getMessage());
    }

    @ExceptionHandler({ExistsByCpfConflictException.class})
    public ResponseEntity<String> handleExistsByCpfConflictException(ExistsByCpfConflictException exception) {
        return ResponseEntity.status(409).body(exception.getMessage());
    }

    @ExceptionHandler({ExistsByNameConflictException.class})
    public ResponseEntity<String> handleExistsByNameConflictException(ExistsByNameConflictException exception) {
        return ResponseEntity.status(409).body(exception.getMessage());
    }

    @ExceptionHandler({NoGamesInStockConflictException.class})
    public ResponseEntity<String> handleNoGamesInStockConflictException(NoGamesInStockConflictException exception) {
        return ResponseEntity.status(422).body(exception.getMessage());
    }

}
