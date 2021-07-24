package com.bank.userregistrationservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;


import com.bank.commonapi.dto.CustomerDTO;
import com.bank.commonapi.dto.LoanDTO;
import com.bank.userregistrationservice.Exception.UserRegistrationServiceException;
import com.bank.userregistrationservice.entity.Customer;
import com.bank.userregistrationservice.entity.Loan;

public interface UserRegistrationService extends UserDetailsService {
	
	public Customer registerCustomer(CustomerDTO customerDTO) throws UserRegistrationServiceException;
	public Loan applyLoan(LoanDTO loanDTO);
	public Customer updateCustomer(CustomerDTO customerDTO);
	public CustomerDTO getCustomerDetails(String userName);

}
