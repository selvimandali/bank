package com.bank.userregistrationservice.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bank.commonapi.events.ApplyLoanEvent;
import com.bank.commonapi.events.UpdateUserEvent;
import com.bank.userregistrationservice.service.UserRegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventListener {
	
	@Autowired
	private UserRegistrationService userRegistrationService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@KafkaListener(topics = {"apply-loan-events"})
	public void applyLoanEventListener(ConsumerRecord<Long, String> consumeRecord) {
		ApplyLoanEvent applyLoanEvent;
		try {
			applyLoanEvent = objectMapper.readValue(consumeRecord.value(), ApplyLoanEvent.class);
			userRegistrationService.applyLoan(applyLoanEvent.getLoanDTO());
		} catch (JsonProcessingException e) {
			log.info("JsonProcessingException");
		}
		
	}
	
	@KafkaListener(topics = {"update-customer-events"})
	public void updateCustomerEventListener(ConsumerRecord<Long, String> consumeRecord) {
		UpdateUserEvent updateUserEvent;
		try {
			updateUserEvent = objectMapper.readValue(consumeRecord.value(), UpdateUserEvent.class);
			userRegistrationService.updateCustomer(updateUserEvent.getCustomerDTO());
		} catch (JsonProcessingException e) {
			log.info("JsonProcessingException");
		}
		
	}

}
