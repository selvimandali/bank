package com.bank.userregistrationservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.commonapi.dto.CustomerDTO;
import com.bank.commonapi.dto.LoanDTO;
import com.bank.userregistrationservice.entity.Customer;
import com.bank.userregistrationservice.entity.Loan;
import com.bank.userregistrationservice.repository.CustomerRepository;
import com.bank.userregistrationservice.repository.LoanRepository;
import com.bank.userregistrationservice.service.impl.UserRegistrationServiceImpl;

class UserRegistrationServiceTest {

	@InjectMocks
	private UserRegistrationServiceImpl userRegistrationServiceImpl;
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private LoanRepository loanRepository;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	public LoanDTO getLoanDTO() {
		return LoanDTO.builder()
				.appliedDate(LocalDate.now())
				.duration(365l)
				.interestRate(0.5)
				.loanAmount(100000l)
				.loanType("Personal Loan")
				.username("selvi")
				.build();
	}
	
	public Loan getLoan() {
		return Loan.builder()
				.appliedDate(LocalDate.now())
				.duration(365l)
				.interestRate(0.5)
				.loanAmount(100000l)
				.loanType("Personal Loan")
				.customer(getCustomer())
				.build();
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
				.contactNumber(encryptData("9666123456"))
				.country("country")
				.dateOfBirth("20-1-2021")
				.emailAddress(encryptData("abcd@gmail.com"))
				.panCard(encryptData("ABCDE1234F"))
				.password("selvi00")
				.state("state")
				.username("selvi")
				.build();
	}
	
	private String encryptData(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes());
	}
	
	public Customer getCustomerWithLoans() {
		Set<Loan> loans= new HashSet<Loan>();
		Customer customer= getCustomer();
		customer.setLoans(loans);
		return customer;
		
	}
	
	@Test
	void testRegisterCustomer() {
		when(customerRepository.save(any(Customer.class))).thenReturn(getCustomer());
		Customer customer = userRegistrationServiceImpl.registerCustomer(getCustomerDTO());
		assertNotNull(customer);
	}

	@Test
	void testApplyLoan() {
		when(customerRepository.findByUsername(anyString())).thenReturn(Optional.of(getCustomer()));
		when(loanRepository.save(any(Loan.class))).thenReturn(getLoan());
		Loan loan = userRegistrationServiceImpl.applyLoan(getLoanDTO());
		assertEquals(loan.getCustomer().getUsername(), getLoanDTO().getUsername());
	}

	@Test
	void testUpdateCustomer() {
		when(customerRepository.findByUsername(anyString())).thenReturn(Optional.of(getCustomerWithLoans()));
		when(customerRepository.save(any(Customer.class))).thenReturn(getCustomerWithLoans());
		Customer customer = userRegistrationServiceImpl.updateCustomer(getCustomerDTO());
		assertNotNull(customer);
		
	}

	@Test
	void testGetCustomerDetails() {
		when(customerRepository.findByUsername(anyString())).thenReturn(Optional.of(getCustomerWithLoans()));
		CustomerDTO customerDTO = userRegistrationServiceImpl.getCustomerDetails("selvi");
		assertEquals(customerDTO.getUserName(),"selvi");
	}

}
