package com.boardcamp.api.services;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.CustomersIdConflictException;
import com.boardcamp.api.exceptions.GamesIdConflictException;
import com.boardcamp.api.exceptions.RentalsIdConflictException;
import com.boardcamp.api.exceptions.ReturnDateConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;

@Service
public class RentalsService {

    final RentalsRepository rentalsRepository;
    final GamesRepository gamesRepository;
    final CustomersRepository customersRepository;

    RentalsService(
                    RentalsRepository rentalsRepository, 
                    GamesRepository gamesRepository, 
                    CustomersRepository customersRepository) {
        this.rentalsRepository = rentalsRepository;
        this.gamesRepository = gamesRepository;
        this.customersRepository = customersRepository;
    }

    public List<RentalsModel> getRentals() {
        return rentalsRepository.findAll();
    }

    public Optional<RentalsModel> getRentalsById(Long id) {
        return rentalsRepository.findById(id);
    }

    public RentalsModel postRentals(RentalsDTO body) {
        GamesModel game = gamesRepository
            .findById(body.getGameId())
            .orElseThrow(() -> new GamesIdConflictException("Game ID does not exist."));
        CustomersModel customer = customersRepository
            .findById(body.getCustomerId())
            .orElseThrow(() -> new CustomersIdConflictException("Customer ID does not exist."));

        RentalsModel rental = new RentalsModel(body, game, customer);
        return rentalsRepository.save(rental);
    }

    public RentalsModel updateRentals(Long id) {
        RentalsModel rental = rentalsRepository
            .findById(id)
            .orElseThrow(() -> new RentalsIdConflictException("Rental ID does not exist."));


        if (rental.getReturnDate() != null) {
            throw new ReturnDateConflictException("Rental has already been returned."); 
        }

        LocalDate today = LocalDate.now();
        rental.setReturnDate(today);

        LocalDate dueDate = rental.getRentDate().plusDays(rental.getDaysRented());
        long delayDays = ChronoUnit.DAYS.between(dueDate, today);
        if (delayDays > 0) {
            rental.setDelayFee((int) delayDays * rental.getGame().getPricePerDay());
        } else {
            rental.setDelayFee(0);
        }

        return rentalsRepository.save(rental);
    }

    public void deleteRentals(Long id) {
        RentalsModel rental = rentalsRepository.findById(id)
            .orElseThrow(() -> new RentalsIdConflictException("Rental ID does not exist."));

        if (rental.getReturnDate() == null) {
            throw new ReturnDateConflictException("Rental has not been returned yet.");
        }

        rentalsRepository.deleteById(id);
    }
}
