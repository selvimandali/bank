package com.bank.commonapi.events;

import com.bank.commonapi.dto.LoanDTO;
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
public class ApplyLoanEvent {
	
	private Long applyLoanEventId;
	
	private EventType eventType;
	
	private LoanDTO loanDTO;

}
