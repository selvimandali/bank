package com.bank.userregistrationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.userregistrationservice.entity.Customer;

@Repository
public interface CustomerRepository extends  JpaRepository<Customer, Long> {
	
	Optional<Customer> findByUsername(String username);

}
