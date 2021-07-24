package com.bank.customerupdateservice.producer;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

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

import com.bank.commonapi.dto.CustomerDTO;
import com.bank.customerupdateservice.exception.CustomerUpdateServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

class UpdateUserEventProducerTest {
	
	@InjectMocks
	private UpdateUserEventProducer updateUserEventProducer;
	
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
	
	public CustomerDTO getCustomerDTO() {
		return CustomerDTO.builder()
				.accountType("Personal Account")
				.address("D-No, street, ward, city, district")
				.contactNumber("9666123456")
				.country("country")
				.dateOfBirth("20-1-2021")
				.emailAddress("abcd@gmail.com")
				.panCard("ABCDE1234F")
				.password("selvi00")
				.state("state")
				.userName("selvi")
				.build();
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	void testProduceEvent() throws CustomerUpdateServiceException, InterruptedException, ExecutionException {
		SettableListenableFuture<SendResult<Long, String>> future = new SettableListenableFuture<SendResult<Long, String>>();
		future.set(getSendResult());
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		SendResult<Long, String> sendResult=updateUserEventProducer.produceEvent(getCustomerDTO());
		assertEquals(getSendResult().getProducerRecord().topic(), sendResult.getProducerRecord().topic());
	}

}
