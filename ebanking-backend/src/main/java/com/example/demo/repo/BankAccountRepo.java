package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.BankAccount;


public interface BankAccountRepo extends JpaRepository<BankAccount,String>{

}
