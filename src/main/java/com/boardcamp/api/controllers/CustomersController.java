package com.boardcamp.api.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.services.CustomersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    
    final CustomersService customersService;
    CustomersController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @GetMapping
    public ResponseEntity<Object> getCustomers() {
        return ResponseEntity.status(HttpStatus.OK).body(customersService.getCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomersById(@PathVariable("id") Long id){
        Optional<CustomersModel> customer = customersService.getCustomersById(id);

        if (!customer.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(customer.get());
        }
    }

    @PostMapping
    public ResponseEntity<Object> postCustomers(@RequestBody @Valid CustomersDTO body) {
        Optional<CustomersModel> customer = customersService.postCustomers(body);

        if (!customer.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Item with this name already exists.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customer.get());
    }
}

        