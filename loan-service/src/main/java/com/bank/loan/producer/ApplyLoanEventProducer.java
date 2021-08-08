package com.bank.loan.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.bank.commonapi.dto.LoanDTO;
import com.bank.commonapi.events.ApplyLoanEvent;
import com.bank.commonapi.events.EventType;
import com.bank.loan.exception.LoanServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplyLoanEventProducer {
	
	private KafkaTemplate<Long, String> kafkaTemplate;
	
	private ObjectMapper objectMapper;
	
	@Value("${topic.applyLoanTopic}")
	String applyLoanTopic;
	
	@Autowired
	public ApplyLoanEventProducer(KafkaTemplate<Long, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper; 
	}

	public SendResult<Long, String> produceEvent(LoanDTO loanDTO) throws LoanServiceException{
		Long key=ThreadLocalRandom.current().nextLong();
		SendResult<Long, String> sendResult = null;
		ApplyLoanEvent applyLoanEvent = ApplyLoanEvent.builder()
				.applyLoanEventId(key)
				.eventType(EventType.APPLY_LOAN)
				.loanDTO(loanDTO)
				.build();
		try {
			String value= objectMapper.writeValueAsString(applyLoanEvent);
			List<Header> headers= List.of(new RecordHeader(applyLoanTopic, loanDTO.getUsername().getBytes()));
			ProducerRecord<Long, String> producerRecord = new ProducerRecord<Long, String>(applyLoanTopic, null, key, value, headers);
			sendResult = kafkaTemplate.send(producerRecord).get();
		} catch (JsonProcessingException e) {
			log.error("JsonProcessingException");
			throw new LoanServiceException(e.getMessage());
		} catch ( ExecutionException e) {
			log.error("ExecutionException");
			throw new LoanServiceException(e.getMessage());
		}catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new LoanServiceException(e.getMessage());
		}
		return sendResult;
	}
}
