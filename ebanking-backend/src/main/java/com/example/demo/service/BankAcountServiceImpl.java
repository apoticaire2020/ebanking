package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.AcountOperation;
import com.example.demo.entities.BankAcount;
import com.example.demo.entities.CurentAcount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAcount;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAcountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mappers.BanckAcountMapperImp;
import com.example.demo.repo.AcounrOperationRepo;
import com.example.demo.repo.BankAcounrRepo;
import com.example.demo.repo.CustomerRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor  // INJECTION DES DEPENDANCES
@Slf4j
public class BankAcountServiceImpl implements BankAcountService{

	private AcounrOperationRepo acounrOperationRepo	;
	private BankAcounrRepo bankAcounrRepo;
	private CustomerRepo customerRepo; 
	private BanckAcountMapperImp mapper;
	//Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	
	@Override
	public Customer saveCustomer(Customer customer) {
		log.info("saving new customer");
		Customer savecustomer=customerRepo.save(customer);
		return savecustomer;
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
	public BankAcount getBankAcount(String accountId) throws BankAcountNotFoundException {
	BankAcount acount=	bankAcounrRepo.findById(accountId).
			orElseThrow(()->new BankAcountNotFoundException("acount not found"));
	
	
	return acount;	
	}

	@Override
	public void debit(String accountId, double amount, String description) throws BankAcountNotFoundException, BalanceNotSufficientException {
		BankAcount bankAcount = getBankAcount(accountId);
		if (bankAcount.getBalance()<amount) {
			throw new BalanceNotSufficientException("balance not sufficient");
		}
		AcountOperation acountOperation = new AcountOperation();
		acountOperation.setAmount(amount);
		acountOperation.setBankAcount(bankAcount);
		acountOperation.setDateOperation(new Date());
		acountOperation.setDescription(description);
		acountOperation.setType(OperationType.DEBIT);
		acounrOperationRepo.save(acountOperation);
		bankAcount.setBalance(bankAcount.getBalance()-amount);
		bankAcounrRepo.save(bankAcount);
	}

	@Override
	public void credit(String accountId, double amount, String description) throws BankAcountNotFoundException, BalanceNotSufficientException {
		BankAcount bankAcount = getBankAcount(accountId);
		
		AcountOperation acountOperation = new AcountOperation();
		acountOperation.setAmount(amount);
		acountOperation.setBankAcount(bankAcount);
		acountOperation.setDateOperation(new Date());
		acountOperation.setDescription(description);
		acountOperation.setType(OperationType.CREDIT);
		acounrOperationRepo.save(acountOperation);
		bankAcount.setBalance(bankAcount.getBalance()+amount);
		bankAcounrRepo.save(bankAcount);
		
	}

	@Override
	public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAcountNotFoundException, BalanceNotSufficientException {
		debit(accountIdSource,amount,"transfert to " +accountIdDestination );
		credit(accountIdDestination, amount, "transfert from "+accountIdSource);
		
	}



	@Override
	public CurentAcount saveCurentBankAcount(double initialBalance, Long customerId, double overDraft)
			throws CustomerNotFoundException {
		Customer customer = customerRepo.findById(customerId).orElse(null);
		if (customer==null) {
			throw new CustomerNotFoundException("customer not exist");
		}
		log.info("saving new bank acount");
		CurentAcount acount = new CurentAcount();
		
		acount.setId(UUID.randomUUID().toString());
		acount.setCreateAt(new Date());
		acount.setBalance(initialBalance);
		acount.setCustomer(customer);
		acount.setOverDraft(overDraft);
		CurentAcount savecurentAcount = bankAcounrRepo.save(acount);
		return savecurentAcount;
	}



	@Override
	public SavingAcount saveSavingBankAcount(double initialBalance, Long customerId, double interestRate)
			throws CustomerNotFoundException {
		Customer customer = customerRepo.findById(customerId).orElse(null);
		if (customer==null) {
			throw new CustomerNotFoundException("customer not exist");
		}
		log.info("saving new bank acount");
		
		SavingAcount acount = new SavingAcount();
		
		acount.setId(UUID.randomUUID().toString());
		acount.setCreateAt(new Date());
		acount.setBalance(initialBalance);
		acount.setCustomer(customer);
		acount.setInterestRate(interestRate);
		SavingAcount saveSavingtAcount = bankAcounrRepo.save(acount);
		return saveSavingtAcount;
	}
	
	@Override
	public List<BankAcount> bankAcountList(){
		return bankAcounrRepo.findAll();
	}

}
