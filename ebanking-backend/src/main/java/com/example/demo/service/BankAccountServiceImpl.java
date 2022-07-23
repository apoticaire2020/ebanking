package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.AccountOperationDto;
import com.example.demo.dtos.BankAccountDto;
import com.example.demo.dtos.CurentBankAccountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.SavingBankAcountDto;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAcountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mappers.BanckAcountMapperImp;
import com.example.demo.repo.AccountOperationRepo;

import com.example.demo.repo.BankAccountRepo;
import com.example.demo.repo.CustomerRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor  // INJECTION DES DEPENDANCES
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

	private AccountOperationRepo acounrOperationRepo	;
	private BankAccountRepo bankAcounrRepo;
	private CustomerRepo customerRepo; 
	private BanckAcountMapperImp mapper;
	//Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	
	
	@Override
	public CustomerDto saveCustomer(CustomerDto customerDto) {
		log.info("saving new customer");
		
		Customer cus=mapper.fromCustomerDto(customerDto);
		Customer savedCustomer= customerRepo.save(cus);
	
		return	mapper.fromCustomer(savedCustomer);
		
	}
	
	@Override
	public CustomerDto updateCustomer(CustomerDto customerDto) {
		log.info("saving new customer");
		
		Customer cus=mapper.fromCustomerDto(customerDto);
		Customer savedCustomer= customerRepo.save(cus);
	
		return	mapper.fromCustomer(savedCustomer);
		
	}
	
	
	@Override
	public void deleteCustomer(Long customerId) {
		customerRepo.deleteById(customerId);
		
	}
	

	@Override
	public List<CustomerDto> listCustomer() {
		List<Customer> customers=customerRepo.findAll();
	    List<CustomerDto> customerDto=customers.stream()
			.map(cust->mapper.fromCustomer(cust))
			.collect(Collectors.toList());
	/*   List<CustomerDto> lis= new ArrayList<CustomerDto>();
	   for (Customer cu : customers) {
		CustomerDto customerDto = mapper.fromCustomer(cu);
		lis.add(customerDto);
	}*/
		return customerDto;
	}

	@Override
	public BankAccountDto getBankAcount(String accountId) throws BankAcountNotFoundException {
	BankAccount acount=	bankAcounrRepo.findById(accountId).
			orElseThrow(()->new BankAcountNotFoundException("acount not found"));
	
	if (acount instanceof SavingAccount) {
		SavingAccount  savingAcount = (SavingAccount) acount;
		return mapper.fromSavingBankAcount(savingAcount);
	}else
	   {
	
		CurentAccount curentAcount   = (CurentAccount) acount;
	   return mapper.fromCurentAcount(curentAcount);
	   }
	}

	@Override
	public void debit(String accountId, double amount, String description) throws BankAcountNotFoundException, BalanceNotSufficientException {
		BankAccount acount=	bankAcounrRepo.findById(accountId).
				orElseThrow(()->new BankAcountNotFoundException("acount not found"));
		if (acount.getBalance()<amount) {
			throw new BalanceNotSufficientException("balance not sufficient");
		}
		AccountOperation acountOperation = new AccountOperation();
		acountOperation.setAmount(amount);
		acountOperation.setBankAcount(acount);
		acountOperation.setDateOperation(new Date());
		acountOperation.setDescription(description);
		acountOperation.setType(OperationType.DEBIT);
		acounrOperationRepo.save(acountOperation);
		acount.setBalance(acount.getBalance()-amount);
		bankAcounrRepo.save(acount);
	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAcountNotFoundException, BalanceNotSufficientException {
		BankAccount acount=	bankAcounrRepo.findById(accountId).
				orElseThrow(()->new BankAcountNotFoundException("acount not found"));
		AccountOperation acountOperation = new AccountOperation();
		acountOperation.setAmount(amount);
		acountOperation.setBankAcount(acount);
		acountOperation.setDateOperation(new Date());
		acountOperation.setDescription(description);
		acountOperation.setType(OperationType.CREDIT);
		acounrOperationRepo.save(acountOperation);
		acount.setBalance(acount.getBalance()+amount);
		bankAcounrRepo.save(acount);
		
	}

	@Override
	public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAcountNotFoundException, BalanceNotSufficientException {
		debit(accountIdSource,amount,"transfert to " +accountIdDestination );
		credit(accountIdDestination, amount, "transfert from "+accountIdSource);
		
	}



	@Override
	public CurentBankAccountDto saveCurentBankAcount(double initialBalance, Long customerId, double overDraft)
			throws CustomerNotFoundException {
		Customer customer = customerRepo.findById(customerId).orElse(null);
		if (customer==null) {
			throw new CustomerNotFoundException("customer not exist");
		}
		log.info("saving new bank acount");
		CurentAccount acount = new CurentAccount();
		
		acount.setAccountId(UUID.randomUUID().toString());
		acount.setCreateAt(new Date());
		acount.setBalance(initialBalance);
		acount.setCustomer(customer);
		acount.setOverDraft(overDraft);
		CurentAccount savecurentAcount = bankAcounrRepo.save(acount);
		return mapper.fromCurentAcount(savecurentAcount);
	}



	@Override
	public SavingBankAcountDto saveSavingBankAcount(double initialBalance, Long customerId, double interestRate)
			throws CustomerNotFoundException {
		Customer customer = customerRepo.findById(customerId).orElse(null);
		if (customer==null) {
			throw new CustomerNotFoundException("customer not exist");
		}
		log.info("saving new bank acount");
		
		SavingAccount acount = new SavingAccount();
		
		acount.setAccountId(UUID.randomUUID().toString());
		acount.setCreateAt(new Date());
		acount.setBalance(initialBalance);
		acount.setCustomer(customer);
		acount.setInterestRate(interestRate);
		SavingAccount saveSavingtAcount = bankAcounrRepo.save(acount);
		return mapper.fromSavingBankAcount(saveSavingtAcount);
	}
	
	@Override
	public List<BankAccountDto> bankAcountList(){
		List<BankAccount> bankAcounts= bankAcounrRepo.findAll();
	    List<BankAccountDto> bankAcountDtos = bankAcounts.stream().map(acount->{
			if (acount instanceof SavingAccount) {
				SavingAccount savingAcount = (SavingAccount) acount;
				return mapper.fromSavingBankAcount(savingAcount);
			}
			else {
				CurentAccount curentAcount = (CurentAccount) acount;
				return mapper.fromCurentAcount(curentAcount);
			}
		}).collect(Collectors.toList());
	  
	  return bankAcountDtos;
	}


     
	@Override
	public CustomerDto getCustomer(Long id) throws CustomerNotFoundException {
				
	Customer cu= customerRepo.findById(id)
	 .orElseThrow(()->new CustomerNotFoundException("introuv"));
	return	mapper.fromCustomer(cu);
	}

//	@Override
//	public List<AccountOperationDto> accountHistorique(String accountId){
//		 List<AccountOperation> acountOperations= acounrOperationRepo.findByBankAccountId(accountId);
//	
//	List<AccountOperationDto> acountOperationDtos=acountOperations.
//			stream().map(op->mapper.fromAcountOperation(op)).
//			collect(Collectors.toList());
//    	return acountOperationDtos;
//	}


	

}
