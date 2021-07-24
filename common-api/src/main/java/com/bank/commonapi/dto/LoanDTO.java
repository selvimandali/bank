package com.bank.commonapi.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class LoanDTO {
	
	private String username;
	
	private String loanType;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate appliedDate;
	
	private double loanAmount;
	
	private double interestRate;
	
	private Long duration;

}
