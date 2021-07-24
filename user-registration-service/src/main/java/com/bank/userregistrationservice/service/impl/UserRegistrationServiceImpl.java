package com.bank.userregistrationservice.service.impl;

import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bank.userregistrationservice.Exception.UserRegistrationServiceException;
import com.bank.commonapi.dto.CustomerDTO;
import com.bank.commonapi.dto.LoanDTO;
import com.bank.userregistrationservice.entity.Customer;
import com.bank.userregistrationservice.entity.Loan;
import com.bank.userregistrationservice.repository.CustomerRepository;
import com.bank.userregistrationservice.repository.LoanRepository;
import com.bank.userregistrationservice.service.UserRegistrationService;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Customer registerCustomer(CustomerDTO customerDTO) throws UserRegistrationServiceException{
		Optional<Customer> optional=customerRepository.findByUsername(customerDTO.getUserName());
		if(optional.isPresent()) {
			throw new UserRegistrationServiceException("User Name Already Exists");
		}
		return buildAndSaveCustomer(customerDTO);
	}

	private Customer buildAndSaveCustomer(CustomerDTO customerDTO) {
		return customerRepository.save(buildCustomer(customerDTO));
	}

	private Customer buildCustomer(CustomerDTO customerDTO) {
		return Customer.builder()
				.username(validateUserName(customerDTO.getUserName()))
				.password(passwordEncoder.encode(validatePassword(customerDTO.getPassword())))
				.address(customerDTO.getAddress())
				.state(customerDTO.getState())
				.country(customerDTO.getCountry())
				.emailAddress(validateEmailId(customerDTO.getEmailAddress()))
				.panCard(validatePanCardNumber(customerDTO.getPanCard()))
				.contactNumber(validateContactNumber(customerDTO.getContactNumber()))
				.dateOfBirth(customerDTO.getDateOfBirth())
				.accountType(validateAccountType(customerDTO.getAccountType()))
				.build();
	}
	
	private String validateUserName(String userName) throws UserRegistrationServiceException{
		if(!StringUtils.isEmpty(userName)) {
			return userName;
		}
		throw new UserRegistrationServiceException("User Name Shouldn't Be Empty");
	}
	
	private String validatePassword(String password) throws UserRegistrationServiceException{
		if(!StringUtils.isEmpty(password)) {
			return password;
		}
		throw new UserRegistrationServiceException("Password Shouldn't Be Empty");
	}
	
	private String validateEmailId(String email) throws UserRegistrationServiceException{
		if(!StringUtils.isEmpty(email)) {
			String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                    "[a-zA-Z0-9_+&*-]+)*@" + 
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                    "A-Z]{2,7}$"; 
                      
			Pattern pattern = Pattern.compile(emailRegex);
			Matcher m = pattern.matcher(email);
			if(m.matches()) {
				return encryptData(email);
			}
			throw new UserRegistrationServiceException("Not A Valid Email");
		}
		throw new UserRegistrationServiceException("Email Shouldn't Be Empty");
	}
	
	private String validateContactNumber(String contactNumber) throws UserRegistrationServiceException{
		if(!StringUtils.isEmpty(contactNumber)) {
			Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
			Matcher m = p.matcher(contactNumber);
			if(m.find()&& m.group().equals(contactNumber))
			{
				return encryptData(contactNumber); 
			}
			throw new UserRegistrationServiceException("Not A Valid Mobile Number");
		}
		throw new UserRegistrationServiceException("Mobile Number Shouldn't Be Empty");
	}
	
	private String validatePanCardNumber(String panCardNumber) throws UserRegistrationServiceException{
		if(!StringUtils.isEmpty(panCardNumber)) {
			String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(panCardNumber);
			if(m.matches()) {
				return encryptData(panCardNumber);
			}
			throw new UserRegistrationServiceException("Not A Valid Pan Card Number");
		}
		throw new UserRegistrationServiceException("Pan Card Number Shouldn't Be Null");
		
	}
	
	private String validateAccountType(String accountType) throws UserRegistrationServiceException{
		if(!StringUtils.isEmpty(accountType)) {
			return accountType;
		}
		throw new UserRegistrationServiceException("Account Type Shouldn't Be Null");
	}
	
	private String encryptData(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes());
	}
	
	private String decryptData(String data) {
		byte[] decodedBytes = Base64.getDecoder().decode(data);
		return new String(decodedBytes);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<Customer> optional=customerRepository.findByUsername(userName);
		if(optional.isPresent()) {
			return 	optional.get(); 
		}
		throw new UserRegistrationServiceException("User Name Not Found");
	}

	@Override
	public Loan applyLoan(LoanDTO loanDTO) {
		Optional<Customer> optional = customerRepository.findByUsername(loanDTO.getUsername());
		Loan savedLoan = null;
		if(optional.isPresent()) {
			Loan loan= Loan.builder()
					.loanType(loanDTO.getLoanType())
					.appliedDate(loanDTO.getAppliedDate())
					.loanAmount(loanDTO.getLoanAmount())
					.interestRate(loanDTO.getInterestRate())
					.duration(loanDTO.getDuration())
					.customer(optional.get())
					.build();
			savedLoan = loanRepository.save(loan);
		}
		return savedLoan;
	}

	@Override
	public Customer updateCustomer(CustomerDTO customerDTO) {
		Optional<Customer> optional=customerRepository.findByUsername(customerDTO.getUserName());
		if(!optional.isPresent()) {
			throw new UserRegistrationServiceException("User Name Does Not Exist");
		}
		Customer customer = buildCustomer(customerDTO);
		customer.setLoans(optional.get().getLoans());
		return customerRepository.save(customer);
	}

	@Override
	public CustomerDTO getCustomerDetails(String userName) {
		CustomerDTO customerDTO = null;
		Optional<Customer> optional=customerRepository.findByUsername(userName);
		if(optional.isPresent()) {
			Customer customer = optional.get();
			Set<LoanDTO> loans = buildLoanDTO(customer);
			customerDTO = CustomerDTO.builder()
					.userName(customer.getUsername())
					.address(customer.getAddress())
					.state(customer.getState())
					.country(customer.getCountry())
					.emailAddress(decryptData(customer.getEmailAddress()))
					.panCard(decryptData(customer.getPanCard()))
					.contactNumber(decryptData(customer.getContactNumber()))
					.dateOfBirth(customer.getDateOfBirth())
					.accountType(customer.getAccountType())
					.loans(loans)
					.build(); 
		}
		return customerDTO;
	}

	private Set<LoanDTO> buildLoanDTO(Customer customer) {
		Set<LoanDTO> loans =new HashSet<LoanDTO>();
		customer.getLoans().forEach(loan -> {
			LoanDTO loanDTO= LoanDTO.builder()
					.loanType(loan.getLoanType())
					.appliedDate(loan.getAppliedDate())
					.interestRate(loan.getInterestRate())
					.duration(loan.getDuration())
					.loanAmount(loan.getLoanAmount())
					.build();
			loans.add(loanDTO);
		});
		return loans;
	}

}
