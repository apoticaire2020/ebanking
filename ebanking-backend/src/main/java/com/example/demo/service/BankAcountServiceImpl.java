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

import com.example.demo.dtos.BankAcountDto;
import com.example.demo.dtos.CurentBankAcountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.SavingBankAcountDto;
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
	public BankAcountDto getBankAcount(String accountId) throws BankAcountNotFoundException {
	BankAcount acount=	bankAcounrRepo.findById(accountId).
			orElseThrow(()->new BankAcountNotFoundException("acount not found"));
	
	if (acount instanceof SavingAcount) {
		SavingAcount  savingAcount = (SavingAcount) acount;
		return mapper.fromSavingBankAcount(savingAcount);
	}else
	   {
	
		CurentAcount curentAcount   = (CurentAcount) acount;
	   return mapper.fromCurentAcount(curentAcount);
	   }
	}

	@Override
	public void debit(String accountId, double amount, String description) throws BankAcountNotFoundException, BalanceNotSufficientException {
		BankAcount acount=	bankAcounrRepo.findById(accountId).
				orElseThrow(()->new BankAcountNotFoundException("acount not found"));
		if (acount.getBalance()<amount) {
			throw new BalanceNotSufficientException("balance not sufficient");
		}
		AcountOperation acountOperation = new AcountOperation();
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
		BankAcount acount=	bankAcounrRepo.findById(accountId).
				orElseThrow(()->new BankAcountNotFoundException("acount not found"));
		AcountOperation acountOperation = new AcountOperation();
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
	public CurentBankAcountDto saveCurentBankAcount(double initialBalance, Long customerId, double overDraft)
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
		
		SavingAcount acount = new SavingAcount();
		
		acount.setId(UUID.randomUUID().toString());
		acount.setCreateAt(new Date());
		acount.setBalance(initialBalance);
		acount.setCustomer(customer);
		acount.setInterestRate(interestRate);
		SavingAcount saveSavingtAcount = bankAcounrRepo.save(acount);
		return mapper.fromSavingBankAcount(saveSavingtAcount);
	}
	
	@Override
	public List<BankAcountDto> bankAcountList(){
		List<BankAcount> bankAcounts= bankAcounrRepo.findAll();
	    List<BankAcountDto> bankAcountDtos = bankAcounts.stream().map(acount->{
			if (acount instanceof SavingAcount) {
				SavingAcount savingAcount = (SavingAcount) acount;
				return mapper.fromSavingBankAcount(savingAcount);
			}
			else {
				CurentAcount curentAcount = (CurentAcount) acount;
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



	

}
