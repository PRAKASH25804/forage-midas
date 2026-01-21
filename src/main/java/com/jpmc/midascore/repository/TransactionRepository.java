package com.jpmc.midascore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpmc.midascore.entity.TransactionRecord;

public interface TransactionRepository
        extends JpaRepository<TransactionRecord, Long> {
}
