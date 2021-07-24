package com.bank.commonapi.events;

import com.bank.commonapi.dto.CustomerDTO;
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
public class UpdateUserEvent {
	
	private Long updateUserEeventId;
	
	private EventType eventType;
	
	private CustomerDTO customerDTO;
	
}
