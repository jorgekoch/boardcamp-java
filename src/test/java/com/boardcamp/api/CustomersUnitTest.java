package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.ExistsByCpfConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;
import com.boardcamp.api.services.CustomersService;
import com.boardcamp.api.services.GamesService;
import com.boardcamp.api.services.RentalsService;

@SpringBootTest
class CustomersUnitTest {
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
	void givenNoCustomers_whenGettingCustomers_thenReturnsEmptyList() {
		// given
		doReturn(Collections.emptyList()).when(customersRepository).findAll();
		
		// when
		List<CustomersModel> result = customersService.getCustomers();

		// then
		verify(customersRepository, times(1)).findAll();
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	void givenExistingCustomers_whenGettingCustomers_thenReturnsCustomersList() {

		CustomersModel customer1 = new CustomersModel();
		CustomersModel customer2 = new CustomersModel();
		List<CustomersModel> mockCustomers = List.of(customer1, customer2);
		

		doReturn(mockCustomers).when(customersRepository).findAll();
		
		// when
		List<CustomersModel> result = customersService.getCustomers();

		// then
		verify(customersRepository, times(1)).findAll();
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(mockCustomers, result);
	}

	@Test
	void givenNoCustomers_whenGettingCustomersById_thenReturnsEmptyList() {
		// given
		doReturn(Optional.empty()).when(customersRepository).findById(any());
		
		// when
		Optional<CustomersModel> result = customersService.getCustomersById(any());

		// then
		verify(customersRepository, times(1)).findById(any());
		assertNotNull(result);
	}
    
	@Test
	void givenExistingCustomer_whenGettingCustomersById_thenReturnsCustomer() {
		// given
		CustomersModel customer = new CustomersModel();
		doReturn(Optional.of(customer)).when(customersRepository).findById(1L);
		
		// when
		Optional<CustomersModel> result = customersService.getCustomersById(1L);

		// then
		verify(customersRepository, times(1)).findById(1L);
		assertNotNull(result);
		assertEquals(customer, result.get());
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


}
