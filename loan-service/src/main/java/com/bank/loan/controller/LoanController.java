package com.bank.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bank.commonapi.dto.LoanDTO;
import com.bank.loan.producer.ApplyLoanEventProducer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Api
@RestController
@Slf4j
@RequestMapping("/loan")
public class LoanController {
	
	@Autowired
	private ApplyLoanEventProducer applyLoanEventProducer;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${customer.url}")
	private String loanUrl;
	
	private String authorization="Authorization";
	
	@PostMapping("/apply")
	public ResponseEntity<String> applyLoan(@RequestBody @Validated LoanDTO loanDTO){
		try {
			applyLoanEventProducer.produceEvent(loanDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Loan Applied Successfully");
	}
	
	@GetMapping("/details/{username}")
	@HystrixCommand(fallbackMethod="loanDetailsFallBack")
	public ResponseEntity<Object> getCustomerLoanDetails(@RequestHeader HttpHeaders requestHeaders, @PathVariable String username){
		log.info("request->"+requestHeaders.get(authorization));
		HttpHeaders headers = new HttpHeaders();
		headers.set(authorization,requestHeaders.get(authorization).get(0));
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> response  = restTemplate.exchange(loanUrl+username, HttpMethod.GET, entity, Object.class);
		return response;
	}
	
	public ResponseEntity<Object> loanDetailsFallBack( HttpHeaders requestHeaders, String username) {
		log.info("requestHeaders->{}",requestHeaders);
		log.info("username->{}",username);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("the service is not avaialable right now");
	}

}
