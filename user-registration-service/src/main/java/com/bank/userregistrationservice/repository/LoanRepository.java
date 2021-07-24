package com.bank.userregistrationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.userregistrationservice.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
