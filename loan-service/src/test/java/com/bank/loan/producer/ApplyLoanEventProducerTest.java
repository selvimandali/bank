package com.bank.loan.producer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.bank.commonapi.dto.LoanDTO;
import com.bank.loan.exception.LoanServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

class ApplyLoanEventProducerTest {
	
	@InjectMocks
	private ApplyLoanEventProducer applyLoanEventProducer;
	
	@Mock
	private KafkaTemplate<Long, String> kafkaTemplate;
	
	@Mock
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	public SendResult<Long, String> getSendResult(){
		ProducerRecord<Long, String> producerRecord = new ProducerRecord<Long, String>("topic", "value");
		RecordMetadata recordMetadata = new RecordMetadata(null, 0l, 0l, 0l, 0l, 0, 0);
		return new SendResult<Long, String>(producerRecord, recordMetadata);
	}
	
	public LoanDTO getLoanDTO() {
		return LoanDTO.builder()
				.appliedDate(LocalDate.now())
				.duration(365l)
				.interestRate(0.5)
				.loanAmount(100000l)
				.loanType("Personal Loan")
				.username("selvi")
				.build();
	}
	
	@SuppressWarnings({ "unchecked" })
	@Test
	void testProduceEvent() throws LoanServiceException, InterruptedException, ExecutionException {
		SettableListenableFuture<SendResult<Long, String>> future = new SettableListenableFuture<SendResult<Long, String>>();
		future.set(getSendResult());
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		SendResult<Long, String> sendResult=applyLoanEventProducer.produceEvent(getLoanDTO());
		assertEquals(getSendResult().getProducerRecord().topic(), sendResult.getProducerRecord().topic());
	}

}
