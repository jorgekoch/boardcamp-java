package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomersIntegrationTest {
    
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
	void givenExistingCpf_whenCreatingCustomer_thenThrowsError() {
        
        // given
        CustomersModel existingCustomer = new CustomersModel(null, "test", "1234567890", "12345678901");
        customersRepository.save(existingCustomer);

        CustomersDTO CustomerDto = new CustomersDTO("test", "1234567890", "12345678901");

        HttpEntity<CustomersDTO> body = new HttpEntity<>(CustomerDto);
       
        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/customers",
            HttpMethod.POST,
            body,
            String.class
        );

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Customer with this CPF already exists", response.getBody());
    }

    @Test
	void givenValidName_whenCreatingCustomer_thenCreatesCustomer() {
                
        // given
        CustomersDTO CustomerDto = new CustomersDTO("test", "1234567890", "12345678901");

        HttpEntity<CustomersDTO> body = new HttpEntity<>(CustomerDto);
       
        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/customers",
            HttpMethod.POST,
            body,
            String.class
        );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
	void givenNoCustomers_whenGettingCustomersById_thenReturnsEmptyList() {
        // given
       
        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/customers/1",
            HttpMethod.GET,
            null,
            String.class
        );

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
	void givenExistingCustomer_whenGettingCustomersById_thenReturnsCustomer() {
         // given
        CustomersModel existingCustomer = new CustomersModel(null, "test", "1234567890", "12345678901");
        customersRepository.save(existingCustomer);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/customers" + "/" + existingCustomer.getId(),
            HttpMethod.GET,
            null,
            String.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void givenNoCustomers_whenGettingCustomers_thenReturnsEmptyList() {
        // given
       
        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/customers",
            HttpMethod.GET,
            null,
            String.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, customersRepository.count());
    }

    @Test
	void givenExistingCustomers_whenGettingCustomers_thenReturnsCustomersList() {
        // given
        CustomersModel existingCustomer = new CustomersModel(null, "test", "1234567890", "12345678901");
        customersRepository.save(existingCustomer);

        // when
        ResponseEntity<List> response = testRestTemplate.exchange(
            "/customers",
            HttpMethod.GET,
            null,
            List.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, customersRepository.count());
    }

}