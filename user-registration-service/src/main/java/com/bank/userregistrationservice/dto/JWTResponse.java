package com.bank.userregistrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JWTResponse {
	
	private final String jwttoken;

}
