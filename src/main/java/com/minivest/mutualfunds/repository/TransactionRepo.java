package com.minivest.mutualfunds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minivest.mutualfunds.entity.Transaction;

@Repository
// one interface can extends another interface
// connection TransactionRepo to Transaction entity and use Long as primary key type
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    
}
