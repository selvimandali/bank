package com.bank.commonapi.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
	
	private String userName;
	
	private String password;
	
	private String address;
	
	private String state;
	
	private String country;
	
	private String emailAddress;
	
	private String panCard;
	
	private String contactNumber;
	
	private String dateOfBirth;
	
	private String accountType;
	
	private Set<LoanDTO> loans;
	
}
