package com.bank.customerupdateservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.bank.commonapi.dto.CustomerDTO;
import com.bank.customerupdateservice.exception.CustomerUpdateServiceException;
import com.bank.customerupdateservice.producer.UpdateUserEventProducer;

class CustomerUpdateServiceControllerTest {
	
	@InjectMocks
	private CustomerUpdateServiceController customerUpdateServiceController;
	
	@Mock
	private UpdateUserEventProducer updateUserEventProducer;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	public CustomerDTO getCustomerDTO() {
		return CustomerDTO.builder()
				.accountType("Personal Account")
				.address("D-No, street, ward, city, district")
				.contactNumber("9666123456")
				.country("country")
				.dateOfBirth("20-1-2021")
				.emailAddress("abcd@gmail.com")
				.panCard("ABCDE1234F")
				.password("selvi00")
				.state("state")
				.userName("selvi")
				.build();
	}
	
	@Test
	void testpdateCustomer() throws CustomerUpdateServiceException, InterruptedException {
		when(updateUserEventProducer.produceEvent(any())).thenReturn(null);
		ResponseEntity<String> respone = customerUpdateServiceController.updateCustomer(getCustomerDTO());
		assertEquals("user Updated Successfully", respone.getBody());
	}
	
	@Test
	void testpdateCustomerException() throws CustomerUpdateServiceException, InterruptedException {
		when(updateUserEventProducer.produceEvent(any())).thenThrow(new CustomerUpdateServiceException("internal server error"));
		ResponseEntity<String> respone = customerUpdateServiceController.updateCustomer(getCustomerDTO());
		assertEquals("internal server error", respone.getBody());
	}

}
