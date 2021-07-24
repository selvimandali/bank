package com.bank.customerupdateservice.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.bank.commonapi.dto.CustomerDTO;
import com.bank.commonapi.events.EventType;
import com.bank.commonapi.events.UpdateUserEvent;
import com.bank.customerupdateservice.exception.CustomerUpdateServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateUserEventProducer {
	@Autowired
	private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	public SendResult<Long, String> produceEvent(CustomerDTO customerDTO) throws CustomerUpdateServiceException, InterruptedException {
		Long key=ThreadLocalRandom.current().nextLong();
		SendResult<Long, String> sendResult = null;
		UpdateUserEvent updateUserEvent = UpdateUserEvent.builder()
				.updateUserEeventId(key)
				.eventType(EventType.UPDATE_USER)
				.customerDTO(customerDTO)
				.build();
		try {
			String value= objectMapper.writeValueAsString(updateUserEvent);
			List<Header> headers= List.of(new RecordHeader("update-customer-service", customerDTO.getUserName().getBytes()));
			ProducerRecord<Long, String> producerRecord = new ProducerRecord<Long, String>("update-customer-events", null, key, value, headers);
			sendResult = kafkaTemplate.send(producerRecord).get();
		} catch (JsonProcessingException e) {
			log.error("JsonProcessingException");
			throw new CustomerUpdateServiceException(e.getMessage());
		} catch ( ExecutionException e) {
			log.error("ExecutionException");
			throw new CustomerUpdateServiceException(e.getMessage());
		}catch(InterruptedException e) {
			log.error("InterruptedException");
			throw e;
		}catch(Exception e) {
			throw new CustomerUpdateServiceException(e.getMessage());
		}
		return sendResult;
	}

}
