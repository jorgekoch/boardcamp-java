package com.boardcamp.api.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.services.RentalsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentals")
public class RentalsController {
    
    final RentalsService rentalsService;
    RentalsController(RentalsService rentalsService) {
        this.rentalsService = rentalsService;
    }

    @GetMapping
    public ResponseEntity<Object> getRentals() {
        return ResponseEntity.status(200).body(rentalsService.getRentals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRentalsById(@PathVariable("id") Long id) {
        Optional<RentalsModel> rental = rentalsService.getRentalsById(id);

        if (!rental.isPresent()) {
            return ResponseEntity.status(404).body("Item not found");
        } else {
            return ResponseEntity.status(200).body(rental.get());
        }
    }  

    @PostMapping("/{id}/return")
    public ResponseEntity<Object> updateRentals(@PathVariable("id") Long id) {
        Optional<RentalsModel> rental = rentalsService.updateRentals(id);
        if (!rental.isPresent()) {
            return ResponseEntity.status(404).body("Item not found");
        } else {
            return ResponseEntity.status(200).body(rental.get());
        }
    }


    @PostMapping
    public ResponseEntity<Object> postRentals(@RequestBody @Valid RentalsDTO body) {
        Optional<RentalsModel> rental = rentalsService.postRentals(body);
        return ResponseEntity.status(201).body(rental);
    }

}
