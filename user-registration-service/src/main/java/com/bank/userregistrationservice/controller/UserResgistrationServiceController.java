package com.bank.userregistrationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.userregistrationservice.Exception.UserRegistrationServiceException;
import com.bank.userregistrationservice.configuration.JWTTokenUtil;
import com.bank.commonapi.dto.CustomerDTO;
import com.bank.userregistrationservice.dto.JWTRequest;
import com.bank.userregistrationservice.dto.JWTResponse;
import com.bank.userregistrationservice.entity.Customer;
import com.bank.userregistrationservice.service.UserRegistrationService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Api
@RestController
@Slf4j
@RequestMapping("/customer")
public class UserResgistrationServiceController {

	private UserRegistrationService userRegistrationService;
	
	private AuthenticationManager authenticationManager;
	
	private JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	public UserResgistrationServiceController(UserRegistrationService userRegistrationService, AuthenticationManager authenticationManager, JWTTokenUtil jwtTokenUtil) {
		this.userRegistrationService = userRegistrationService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody @Validated CustomerDTO customer){
		log.info("saving the employee into the database [{}]", customer.toString());
		try {
			Customer savedCustomer = userRegistrationService.registerCustomer(customer);
			if(savedCustomer!=null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer.getUsername());
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not Registered, Please Try Again");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> loginCustomer(@RequestBody JWTRequest jwtRequest){
		try {
			authenticate(jwtRequest.getUserName(),jwtRequest.getPassword());
			final UserDetails userDetails = userRegistrationService
					.loadUserByUsername(jwtRequest.getUserName());
			final String token = jwtTokenUtil.generateToken(userDetails);
			log.info("jwttoken->"+token);
			return ResponseEntity.status(HttpStatus.OK).body( JWTResponse.builder()
					.jwttoken(token)
					.build());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	private void authenticate(String userName,String password) {
		try {
			 authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userName, password));
		}catch(BadCredentialsException e) {
			throw new UserRegistrationServiceException("Invalid Credentials");
		}
	}
	
	@GetMapping("/loans/{username}")
	public ResponseEntity<Object> getCustomerLoans(@PathVariable String username){
		CustomerDTO customerDTO = userRegistrationService.getCustomerDetails(username);
		return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
	}
}
