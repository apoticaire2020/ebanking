package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.BankAcount;
import com.example.demo.entities.CurentAcount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAcount;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAcountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;

public interface BankAcountService {

	Customer saveCustomer(Customer customer);
	CurentAcount saveCurentBankAcount(double initialBalance,Long customerId,double overDraft) throws CustomerNotFoundException;
	SavingAcount saveSavingBankAcount(double initialBalance,Long customerId,double interestRate) throws CustomerNotFoundException;
    List<Customer> listCustomer();	
	BankAcount getBankAcount(String accountId) throws BankAcountNotFoundException;
	void debit(String accountId , double amount , String description) throws BankAcountNotFoundException, BalanceNotSufficientException;
	void credit(String accountId , double amount , String description) throws BankAcountNotFoundException, BalanceNotSufficientException;
	void transfert (String accountIdSource,String accountIdDestination , double amount ) throws BankAcountNotFoundException, BalanceNotSufficientException;
	List<BankAcount> bankAcountList();
	
}
