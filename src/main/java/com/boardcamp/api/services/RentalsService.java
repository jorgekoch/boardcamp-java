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

    public Optional<RentalsModel> postRentals(RentalsDTO body) {
        Optional<GamesModel> game = gamesRepository.findById(body.getGameId());
        Optional<CustomersModel> customer = customersRepository.findById(body.getCustomerId());

        if(!game.isPresent() || !customer.isPresent()) {
            return Optional.empty();
        }

        RentalsModel rental = new RentalsModel(body, game.get(), customer.get());
        rentalsRepository.save(rental);
        return Optional.of(rental); 
    }

public Optional<RentalsModel> updateRentals(Long id) {
    Optional<RentalsModel> rentalOpt = rentalsRepository.findById(id);

    if (rentalOpt.isEmpty()) {
        return Optional.empty();
    }

    RentalsModel rental = rentalOpt.get();

    if (rental.getReturnDate() != null) {
        return Optional.empty(); 
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

    rentalsRepository.save(rental);

    return Optional.of(rental);
}

}
