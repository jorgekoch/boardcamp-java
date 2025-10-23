package com.boardcamp.api;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.boardcamp.api.exceptions.ExistsByCpfConflictException;
import com.boardcamp.api.exceptions.GamesIdConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
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
	void givenExistingName_whenCreatingGame_thenThrowsError() {
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
		GamesModel result = gamesService.postGames(game).get();

		// then
		verify(gamesRepository, times(1)).existsByName(any());
		verify(gamesRepository, times(1)).save(any());
		assertEquals(gameModel, result);
	}

	@Test
	void givenExistingCpf_whenCreatingCustomer_thenThrowsError() {
		// given
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");

		doReturn(true).when(customersRepository).existsByCpf(any());

		// when
		ExistsByCpfConflictException exception = assertThrows(
			ExistsByCpfConflictException.class,
			() -> customersService.postCustomers(customer));

		// then
		verify(customersRepository, times(1)).existsByCpf(any());
		assertNotNull(exception);
		assertEquals("Customer with this CPF already exists", exception.getMessage());



}

	@Test
	void givenValidName_whenCreatingCustomer_thenCreatesCustomer() {
		// given
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");
		CustomersModel customerModel = new CustomersModel(customer);

		doReturn(false).when(customersRepository).existsByCpf(any());
		doReturn(customerModel).when(customersRepository).save(any());


		// when
		CustomersModel result = customersService.postCustomers(customer).get();

		// then
		verify(customersRepository, times(1)).existsByCpf(any());
		verify(customersRepository, times(1)).save(any());
		assertEquals(customerModel, result);



}

	@Test
	void givenWrongGameId_whenCreatingRental_thenThrowsError() {
		// given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);

		doReturn(false).when(gamesRepository).findById(any());

		// when
		GamesIdConflictException exception = assertThrows(
			GamesIdConflictException.class,
			() -> rentalsService.postRentals(rental));

		// then
		verify(gamesRepository, times(1)).findById(any());
		assertNotNull(exception);
		assertEquals("Game ID does not exist.", exception.getMessage());
	}
}
