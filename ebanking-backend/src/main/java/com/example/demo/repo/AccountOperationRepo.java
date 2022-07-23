package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.AccountOperation;



public interface AccountOperationRepo extends JpaRepository<AccountOperation,Long>{

	// List<AccountOperation> findByBankAccountId(String accountId);
}
