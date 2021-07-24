package com.bank.customerupdateservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.commonapi.dto.CustomerDTO;
import com.bank.customerupdateservice.producer.UpdateUserEventProducer;
import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/update")
public class CustomerUpdateServiceController {
	
	@Autowired
	private UpdateUserEventProducer updateUserEventProducer;
	
	@PostMapping("/user")
	public ResponseEntity<String> updateCustomer(@RequestBody @Validated CustomerDTO customerDTO){
		try {
			updateUserEventProducer.produceEvent(customerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("user Updated Successfully");
	}

}
