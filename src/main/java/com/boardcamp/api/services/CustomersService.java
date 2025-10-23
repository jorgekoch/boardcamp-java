package com.boardcamp.api.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.CustomersIdConflictException;
import com.boardcamp.api.exceptions.ExistsByCpfConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;

@Service
public class CustomersService {
    
    final CustomersRepository customersRepository;
    CustomersService(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public Object getCustomers() {
        return customersRepository.findAll();
    }

    public Optional<CustomersModel> getCustomersById(Long id) {
        return customersRepository.findById(id);
    }

    public Optional<CustomersModel> postCustomers(CustomersDTO body) {

        if (customersRepository.existsByCpf(body.getCpf())) {
            throw new ExistsByCpfConflictException("Customer with this CPF already exists");
        }

        CustomersModel customer = new CustomersModel(body);
        customersRepository.save(customer);
        return Optional.of(customer);
    }
}
