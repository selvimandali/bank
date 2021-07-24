package com.bank.userregistrationservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import com.bank.commonapi.dto.CustomerDTO;
import com.bank.userregistrationservice.Exception.UserRegistrationServiceException;
import com.bank.userregistrationservice.configuration.JWTTokenUtil;
import com.bank.userregistrationservice.dto.JWTRequest;
import com.bank.userregistrationservice.dto.JWTResponse;
import com.bank.userregistrationservice.entity.Customer;
import com.bank.userregistrationservice.service.UserRegistrationService;

class UserResgistrationServiceControllerTest {
	@InjectMocks
	private UserResgistrationServiceController userResgistrationServiceController;
	
	@Mock
	private UserRegistrationService userRegistrationService;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private JWTTokenUtil jwtTokenUtil;
	

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
	public Customer getCustomer() {
		return Customer.builder()
				.accountType("Personal Account")
				.address("D-No, street, ward, city, district")
				.contactNumber("9666123456")
				.country("country")
				.dateOfBirth("20-1-2021")
				.emailAddress("abcd@gmail.com")
				.panCard("ABCDE1234F")
				.password("selvi00")
				.state("state")
				.username("selvi")
				.build();
	}
	
	public JWTRequest getJWTRequest() {
		return JWTRequest.builder()
				.userName("selvi")
				.password("selvi00")
				.build();
	}

	public JWTResponse getJWTResponse() {
		return JWTResponse.builder()
				.jwttoken("jwttoken")
				.build();
	}
	@Test
	void testRegisterCustomer() {
		when(userRegistrationService.registerCustomer(any())).thenReturn(getCustomer());
		ResponseEntity<String> response = userResgistrationServiceController.registerCustomer(getCustomerDTO());
		assertEquals(response.getBody(), "selvi");
		
	}
	
	@Test
	void testRegisterCustomerError() {
		when(userRegistrationService.registerCustomer(any())).thenReturn(null);
		ResponseEntity<String> response = userResgistrationServiceController.registerCustomer(getCustomerDTO());
		assertEquals( "User not Registered, Please Try Again",response.getBody());
		
	}
	
	@Test
	void testRegisterCustomerException() {
		when(userRegistrationService.registerCustomer(any())).thenThrow(new UserRegistrationServiceException("Internal Server error"));
		ResponseEntity<String> response = userResgistrationServiceController.registerCustomer(getCustomerDTO());
		assertEquals( "Internal Server error",response.getBody());
		
	}

	@Test
	void testLoginCustomer() {
		when(jwtTokenUtil.generateToken(any())).thenReturn("jwttoken");
		ResponseEntity<Object> response =userResgistrationServiceController.loginCustomer(getJWTRequest());
		assertEquals(getJWTResponse(), response.getBody());
	}
	
	@Test
	void testLoginCustomerException() {
		when(jwtTokenUtil.generateToken(any())).thenThrow(new UserRegistrationServiceException("Internal Server error"));
		ResponseEntity<Object> response =userResgistrationServiceController.loginCustomer(getJWTRequest());
		assertEquals("Internal Server error", response.getBody());
	}


	@Test
	void testGetCustomerLoans() {
		when(userRegistrationService.getCustomerDetails(any())).thenReturn(getCustomerDTO());
		ResponseEntity<Object> response = userResgistrationServiceController.getCustomerLoans("selvi");
		assertEquals(getCustomerDTO(), response.getBody());
	}

}
