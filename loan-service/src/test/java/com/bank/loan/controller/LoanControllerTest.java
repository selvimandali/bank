package com.bank.loan.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.bank.commonapi.dto.CustomerDTO;
import com.bank.commonapi.dto.LoanDTO;
import com.bank.loan.exception.LoanServiceException;
import com.bank.loan.producer.ApplyLoanEventProducer;

class LoanControllerTest {
	
	@InjectMocks
	private LoanController loanController;
	
	@Mock
	private ApplyLoanEventProducer applyLoanEventProducer;
	
	@Mock
	private RestTemplate restTemplate;
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	public SendResult<Long, String> getSendResult(){
		ProducerRecord<Long, String> producerRecord = new ProducerRecord<Long, String>("topic", "value");
		RecordMetadata recordMetadata = new RecordMetadata(null, 0l, 0l, 0l, 0l, 0, 0);
		return new SendResult<Long, String>(producerRecord, recordMetadata);
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
	public CustomerDTO getCustomerDTO() {
		Set<LoanDTO> loans = new HashSet<LoanDTO>();
		loans.add(getLoanDTO());
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
				.loans(loans)
				.build();
	}

	@Test
	void testApplyLoan() throws LoanServiceException, InterruptedException {
		when(applyLoanEventProducer.produceEvent(any())).thenReturn(getSendResult());
		ResponseEntity<String> respone = loanController.applyLoan(getLoanDTO());
		assertEquals("Loan Applied Successfully", respone.getBody());
	}
	
	@Test
	void testApplyLoanException() throws LoanServiceException, InterruptedException {
		when(applyLoanEventProducer.produceEvent(any())).thenThrow(new LoanServiceException("internal server error"));
		ResponseEntity<String> respone = loanController.applyLoan(getLoanDTO());
		assertEquals("internal server error", respone.getBody());
	}

	@Test
	void testGetCustomerLoanDetails() {
		HttpHeaders requestHeaders = getRequestHeaders();
		ResponseEntity<Object> resEntity =ResponseEntity.status(HttpStatus.OK).body(getCustomerDTO()); 
		when(restTemplate.exchange(any(String.class),any(HttpMethod.class),any(HttpEntity.class), eq(Object.class))).thenReturn(resEntity);
		ResponseEntity<Object> response = loanController.getCustomerLoanDetails(requestHeaders, "selvi");
		assertEquals(getCustomerDTO(),response.getBody());
	}

	private HttpHeaders getRequestHeaders() {
		HttpHeaders requestHeaders= new HttpHeaders();
		requestHeaders.set("Authorization", "jwttoken");
		return requestHeaders;
	}
	@Test
	void testLoanDetailsFallBack() {
		HttpHeaders requestHeaders = getRequestHeaders();
		ResponseEntity<Object> resEntity =ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		when(restTemplate.exchange(any(String.class),any(HttpMethod.class),any(HttpEntity.class), eq(Object.class))).thenReturn(resEntity);
		ResponseEntity<Object> response = loanController.loanDetailsFallBack(requestHeaders, "selvi");
		assertEquals("the service is not avaialable right now", response.getBody());
	}

}
