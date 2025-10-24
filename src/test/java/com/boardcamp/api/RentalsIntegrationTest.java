package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RentalsIntegrationTest {
 
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private RentalsRepository rentalsRepository;

    @BeforeEach
    void cleanUp() {
        rentalsRepository.deleteAll();
        gamesRepository.deleteAll();
        customersRepository.deleteAll();
    }

    @Test
    void givenExistingRentals_whenGettingAllRentals_thenReturnList() {
        // given
        CustomersModel customer = customersRepository.save(new CustomersModel(
            null, 
            "Test", 
            "12345678900", 
            "12345678910")
            );
        GamesModel game = gamesRepository.save(new GamesModel(
            null, 
            "test", 
            "url", 
            5, 
            10)
            );
        RentalsModel rental = rentalsRepository.save(new RentalsModel(
            null, 
            LocalDate.now(), 
            3, 
            null, 
            30, 
            0, 
            customer, 
            game)
            );

        // when
        ResponseEntity<RentalsModel[]> response = testRestTemplate.exchange(
            "/rentals", 
            HttpMethod.GET, 
            null, 
            RentalsModel[].class
            );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, List.of(response.getBody()).size());
    }

    @Test
    void givenNoRentals_whenGettingAllRentals_thenReturnEmptyList() {
        // when
        ResponseEntity<RentalsModel[]> response = testRestTemplate.exchange(
            "/rentals", 
            HttpMethod.GET, 
            null, 
            RentalsModel[].class
            );
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());  
        assertEquals(0, List.of(response.getBody()).size());
    }

    @Test
    void givenNonExistingRentalId_whenGettingRentalById_thenReturnEmpty() {
        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/rentals/1", 
            HttpMethod.GET, 
            null, 
            String.class
            );  
        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Rental ID does not exist.", response.getBody());
    }

    @Test
    void givenExistingRentalId_whenGettingRentalById_thenReturnRental() {
         // given
        CustomersModel customer = customersRepository.save(new CustomersModel(
            null, 
            "Test", 
            "12345678900", 
            "12345678910")
            );
        GamesModel game = gamesRepository.save(new GamesModel(
            null, 
            "test", 
            "url", 
            5, 
            10)
            );
        RentalsModel rental = rentalsRepository.save(new RentalsModel(
            null, 
            LocalDate.now(), 
            3, 
            null, 
            30, 
            0, 
            customer, 
            game)
            );

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/rentals/" + rental.getId(), 
            HttpMethod.GET, 
            null, 
            String.class
            );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, List.of(response.getBody()).size());
    }

    @Test
	void givenValidIds_whenCreatingRental_thenCreatesRental() {
                // given
        CustomersModel customer = customersRepository.save(new CustomersModel(
            null, 
            "Test", 
            "12345678900", 
            "12345678910")
            );
        GamesModel game = gamesRepository.save(new GamesModel(
            null, 
            "test", 
            "url", 
            5, 
            10)
            );

        RentalsDTO rental = new RentalsDTO(
            customer.getId(), 
            game.getId(), 
            3
            );

        HttpEntity<RentalsDTO> request = new HttpEntity<>(rental);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/rentals", 
            HttpMethod.POST, 
            request, 
            String.class
            );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    void givenValidRentalId_whenUpdatingRental_thenUpdatesRental() {
        // given
        CustomersModel customer = customersRepository.save(new CustomersModel(
            null, 
            "Test", 
            "12345678900", 
            "12345678910")
            );
        GamesModel game = gamesRepository.save(new GamesModel(
            null, 
            "test", 
            "url", 
            5, 
            10)
            );
        RentalsModel rental = rentalsRepository.save(new RentalsModel(
            null, 
            LocalDate.now().minusDays(5), 
            3, 
            null, 
            30, 
            0, 
            customer, 
            game)
            );

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/rentals/" + rental.getId() + "/return", 
            HttpMethod.POST, 
            null, 
            String.class
            );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RentalsModel updatedRental = rentalsRepository.findById(rental.getId()).get();
        assertEquals(LocalDate.now(), updatedRental.getReturnDate());
        assertEquals(20, updatedRental.getDelayFee());
    }

}   
