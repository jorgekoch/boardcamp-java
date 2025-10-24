package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.CustomersIdConflictException;
import com.boardcamp.api.exceptions.GamesIdConflictException;
import com.boardcamp.api.exceptions.RentalsIdConflictException;
import com.boardcamp.api.exceptions.ReturnDateConflictException;
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
class RentalsUnitTest {

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
    void givenExistingRentals_whenGettingAllRentals_thenReturnList() {
        // Given 
        RentalsModel rental1 = new RentalsModel();
        RentalsModel rental2 = new RentalsModel();
        List<RentalsModel> mockRentals = List.of(rental1, rental2);

        doReturn(mockRentals).when(rentalsRepository).findAll();

        // When
        List<RentalsModel> result = rentalsService.getRentals();

        // Then 
        verify(rentalsRepository, times(1)).findAll(); 
        assertNotNull(result);                         
        assertEquals(2, result.size());               
        assertEquals(mockRentals, result);            
    }
    
    @Test
    void givenNoRentals_whenGettingAllRentals_thenReturnEmptyList() {
        // Given
        doReturn(Collections.emptyList()).when(rentalsRepository).findAll();

        // When
        List<RentalsModel> result = rentalsService.getRentals();

        // Then
        verify(rentalsRepository, times(1)).findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNonExistingRentalId_whenGettingRentalById_thenReturnEmpty() {
        // Given
        doReturn(Optional.empty()).when(rentalsRepository).findById(1L);

        // When
        Optional<RentalsModel> result = rentalsService.getRentalsById(1L);

        // Then
        verify(rentalsRepository, times(1)).findById(1L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenExistingRentalId_whenGettingRentalById_thenReturnRental() {
        // Given
        RentalsModel rental = new RentalsModel();
        doReturn(Optional.of(rental)).when(rentalsRepository).findById(1L);

        // When
        Optional<RentalsModel> result = rentalsService.getRentalsById(1L);

        // Then
        verify(rentalsRepository, times(1)).findById(1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(rental, result.get());
    }

	@Test
	void givenWrongGameId_whenCreatingRental_thenThrowsError() {
		// given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);

		doReturn(Optional.empty()).when(gamesRepository).findById(any());

		// when
		GamesIdConflictException exception = assertThrows(
			GamesIdConflictException.class,
			() -> rentalsService.postRentals(rental));

		// then
		verify(gamesRepository, times(1)).findById(any());
		assertNotNull(exception);
		assertEquals("Game ID does not exist.", exception.getMessage());
	}

	@Test
	void givenWrongCustomerId_whenCreatingRental_thenThrowsError() {
		// given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);
		GamesModel gameModel = new GamesModel(game);

		doReturn(Optional.of(gameModel)).when(gamesRepository).findById(any());
		doReturn(Optional.empty()).when(customersRepository).findById(any());

		// when
		CustomersIdConflictException exception = assertThrows(
			CustomersIdConflictException.class,
			() -> rentalsService.postRentals(rental));

		// then
		verify(gamesRepository, times(1)).findById(any());
		verify(customersRepository, times(1)).findById(any());
		assertNotNull(exception);
		assertEquals("Customer ID does not exist.", exception.getMessage());
	}

	@Test
	void givenValidIds_whenCreatingRental_thenCreatesRental() {
		// given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);
		GamesModel gameModel = new GamesModel(game);
		CustomersModel customerModel = new CustomersModel(customer);
		RentalsModel rentalModel = new RentalsModel(rental, gameModel, customerModel);

		doReturn(Optional.of(gameModel)).when(gamesRepository).findById(any());
		doReturn(Optional.of(customerModel)).when(customersRepository).findById(any());
		doReturn(rentalModel).when(rentalsRepository).save(any());

		// when
		RentalsModel result = rentalsService.postRentals(rental);

		// then
		verify(gamesRepository, times(1)).findById(any());
		verify(customersRepository, times(1)).findById(any());
		verify(rentalsRepository, times(1)).save(any());
		assertEquals(rentalModel, result);
	
	}

    @Test
    void givenWrongRentalId_whenUpdatingRental_thenThrowsError() {
        // given
		GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);
		GamesModel gameModel = new GamesModel(game);
		CustomersModel customerModel = new CustomersModel(customer);
		RentalsModel rentalModel = new RentalsModel(rental, gameModel, customerModel);

        doReturn(Optional.empty()).when(rentalsRepository).findById(any());

        // when
        RentalsIdConflictException exception = assertThrows(
            RentalsIdConflictException.class,
            () -> rentalsService.updateRentals(1L));

        // then
        verify(rentalsRepository, times(1)).findById(any());
        assertNotNull(exception);
        assertEquals("Rental ID does not exist.", exception.getMessage());
    }

    @Test
    void givenReturnedRental_whenUpdatingRental_thenThrowsError() {
        // given
        GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);
		GamesModel gameModel = new GamesModel(game);
		CustomersModel customerModel = new CustomersModel(customer);
		RentalsModel rentalModel = new RentalsModel(rental, gameModel, customerModel);

        rentalModel.setReturnDate(LocalDate.now());


        doReturn(Optional.of(rentalModel)).when(rentalsRepository).findById(any());

        // when
        ReturnDateConflictException exception = assertThrows(
            ReturnDateConflictException.class,
            () -> rentalsService.updateRentals(1L));

        // then
        verify(rentalsRepository, times(1)).findById(any());
        assertNotNull(exception);
        assertEquals("Rental has already been returned.", exception.getMessage());
    }

    @Test
    void givenValidRentalId_whenUpdatingRental_thenUpdatesRental() {
        // given
        GamesDTO game = new GamesDTO("Test", "Test", 5, 10);
		CustomersDTO customer = new CustomersDTO("Test", "12345678900", "2000-01-01");	
		RentalsDTO rental = new RentalsDTO(1L, 1L, 5);
		GamesModel gameModel = new GamesModel(game);
		CustomersModel customerModel = new CustomersModel(customer);
		RentalsModel rentalModel = new RentalsModel(rental, gameModel, customerModel);

        doReturn(Optional.of(rentalModel)).when(rentalsRepository).findById(any());
        doReturn(rentalModel).when(rentalsRepository).save(any());

        // when
        RentalsModel result = rentalsService.updateRentals(1L);

        // then
        verify(rentalsRepository, times(1)).findById(any());
        verify(rentalsRepository, times(1)).save(any());
        assertEquals(rentalModel, result);
    }
}
