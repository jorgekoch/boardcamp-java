package com.boardcamp.api;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.CustomersIdConflictException;
import com.boardcamp.api.exceptions.ExistsByCpfConflictException;
import com.boardcamp.api.exceptions.GamesIdConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;
import com.boardcamp.api.services.CustomersService;
import com.boardcamp.api.services.GamesService;
import com.boardcamp.api.services.RentalsService;

@SpringBootTest
class GamesUnitTests {

	@InjectMocks
	private GamesService gamesService;	

	@InjectMocks
	private CustomersService customersService;

	@InjectMocks
	private RentalsService rentalsService;

	@Mock
	private GamesRepository gamesRepository;

	@Mock
	private CustomersRepository customersRepository;

	@Mock
	private RentalsRepository rentalsRepository;


	@Test
	void givenNoGames_whenGettingGames_thenReturnsEmptyList() {
		// given
		doReturn(Collections.emptyList()).when(gamesRepository).findAll();
		
		// when
		List<GamesModel> result = gamesService.getGames();

		// then
		verify(gamesRepository, times(1)).findAll();
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	void givenExistingGames_whenGettingGames_thenReturnsGamesList() {
		// given
		GamesModel game1 = new GamesModel();
		GamesModel game2 = new GamesModel();
		List<GamesModel> mockGames = List.of(game1, game2);

		doReturn(mockGames).when(gamesRepository).findAll();
		
		// when
		List<GamesModel> result = gamesService.getGames();

		// then
		verify(gamesRepository, times(1)).findAll();
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(mockGames, result);
	}
	
	@Test
	void givenExistingSameName_whenCreatingGame_thenThrowsError() {
		// given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);

		doReturn(true).when(gamesRepository).existsByName(any());
		
		// when
		GamesIdConflictException exception = assertThrows(
			GamesIdConflictException.class,
			() -> gamesService.postGames(game));

		// then
		verify(gamesRepository, times(1)).existsByName(any());
		assertNotNull(exception);
		assertEquals("Game with this name already exists", exception.getMessage());
	}

	@Test
	void givenValidGame_whenCreatingGame_thenCreatesGame() {
		// given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		GamesModel gameModel = new GamesModel(game);

		doReturn(false).when(gamesRepository).existsByName(any());
		doReturn(gameModel).when(gamesRepository).save(any());
		
		// when
		GamesModel result = gamesService.postGames(game);

		// then
		verify(gamesRepository, times(1)).existsByName(any());
		verify(gamesRepository, times(1)).save(any());
		assertEquals(gameModel, result);
	}
}