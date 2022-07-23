package com.example.demo.service;

import java.util.List;

import com.example.demo.dtos.AccountOperationDto;
import com.example.demo.dtos.BankAccountDto;
import com.example.demo.dtos.CurentBankAccountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.SavingBankAcountDto;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAcountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;

public interface BankAccountService {

	CustomerDto saveCustomer(CustomerDto customerDto);
	CustomerDto updateCustomer(CustomerDto customerDto);
	void deleteCustomer(Long customerId);
	CustomerDto getCustomer(Long id) throws CustomerNotFoundException;
	
	
	CurentBankAccountDto saveCurentBankAcount(double initialBalance,Long customerId,double overDraft) throws CustomerNotFoundException;
	SavingBankAcountDto saveSavingBankAcount(double initialBalance,Long customerId,double interestRate) throws CustomerNotFoundException;
    
	List<CustomerDto> listCustomer();	
    List<BankAccountDto> bankAcountList();
    
    BankAccountDto getBankAcount(String accountId) throws BankAcountNotFoundException;
	
    void debit(String accountId , double amount , String description) throws BankAcountNotFoundException, BalanceNotSufficientException;
	void credit(String accountId , double amount , String description) throws BankAcountNotFoundException, BalanceNotSufficientException;
	void transfert (String accountIdSource,String accountIdDestination , double amount ) throws BankAcountNotFoundException, BalanceNotSufficientException;
	//CustomerDto getCustomer(Long id) throws CustomerNotFoundException;
	//List<AccountOperationDto> accountHistorique(String acountId);
	

	

	

	
	
}
