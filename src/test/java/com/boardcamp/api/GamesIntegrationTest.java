package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GamesIntegrationTest {
    
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
	void givenNoGames_whenGettingGames_thenReturnsEmptyList() {
        // given
       
        // when
        ResponseEntity<List> response = testRestTemplate.exchange(
            "/games",
            HttpMethod.GET,
            null,
            List.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, gamesRepository.count());
    }

    @Test
	void givenExistingGames_whenGettingGames_thenReturnsGamesList() {
        // given
        GamesModel existingGame = new GamesModel(null, "Test", "image.png", 10, 5);
        gamesRepository.save(existingGame);

        // when
        ResponseEntity<List> response = testRestTemplate.exchange(
            "/games",
            HttpMethod.GET,
            null,
            List.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, gamesRepository.count());
    }

    @Test
    void givenExistingSameName_whenCreatingGame_thenThrowsError() {
        // given
        GamesModel existingGame = new GamesModel(null, "Test", "image.png", 10, 5);
        gamesRepository.save(existingGame);

        GamesDTO newGame = new GamesDTO("Test", "image2.png", 15, 3);
        HttpEntity<GamesDTO> body = new HttpEntity<>(newGame);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(
            "/games",
            HttpMethod.POST,
            body,
            String.class
        );

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(1, gamesRepository.count());
    }

    @Test
	void givenValidGame_whenCreatingGame_thenCreatesGame() {
        // given
        GamesDTO newGame = new GamesDTO("NewGame", "image.png", 20, 7);
        HttpEntity<GamesDTO> body = new HttpEntity<>(newGame);

        // when
        ResponseEntity<GamesModel> response = testRestTemplate.exchange(
            "/games",
            HttpMethod.POST,
            body,
            GamesModel.class
        );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gamesRepository.count());
    }


}
