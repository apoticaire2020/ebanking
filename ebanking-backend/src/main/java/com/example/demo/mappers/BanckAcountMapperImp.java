package com.example.demo.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountOperationDto;
import com.example.demo.dtos.CurentBankAccountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.SavingBankAcountDto;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.CurentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;

@Service
public class BanckAcountMapperImp {

	public CustomerDto fromCustomer(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		BeanUtils.copyProperties(customer, customerDto);
				return customerDto;
	}
	public Customer fromCustomerDto(CustomerDto customerdto) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerdto, customer);
		       return customer;
	}
	
	public CurentBankAccountDto	fromCurentAcount(CurentAccount acount) {
		CurentBankAccountDto acountDto = new CurentBankAccountDto();
		BeanUtils.copyProperties(acount, acountDto);
		acountDto.setCustomerDto(fromCustomer(acount.getCustomer()));
		acountDto.setType(acount.getClass().getSimpleName());
		      return acountDto;
	}
	public CurentAccount	fromCurentAcountDto(CurentBankAccountDto acount) {
		CurentAccount curentAcount = new CurentAccount();
		BeanUtils.copyProperties(acount, curentAcount);
		curentAcount.setCustomer(fromCustomerDto(acount.getCustomerDto()));
		      return curentAcount;
	}
	public SavingBankAcountDto fromSavingBankAcount(SavingAccount savingAcount) {
		SavingBankAcountDto savingBankAcountDto = new SavingBankAcountDto();
		BeanUtils.copyProperties(savingAcount, savingBankAcountDto);
		savingBankAcountDto.setCustomerDto(fromCustomer(savingAcount.getCustomer()));
		savingBankAcountDto.setType(savingAcount.getClass().getSimpleName());
		return savingBankAcountDto;
		
	}
	public SavingAccount fromSavingBankAcountDto(SavingBankAcountDto savingBankAcountDto) {
		SavingAccount savingAcount = new SavingAccount();
		BeanUtils.copyProperties(savingBankAcountDto, savingAcount);
		savingAcount.setCustomer(fromCustomerDto(savingBankAcountDto.getCustomerDto()));
		    return savingAcount;
		
	}
    public AccountOperationDto fromAcountOperation(AccountOperation acountOperation) {
    	AccountOperationDto acountOperationDto= new AccountOperationDto();
    	BeanUtils.copyProperties(acountOperation, acountOperationDto);
    	return acountOperationDto;
    }
//    public AccountOperation fromAcountOperationDto(AccountOperationDto acountOperationDto) {
//    	AccountOperation acountOperation= new AccountOperation();
//    	BeanUtils.copyProperties(acountOperationDto, acountOperation);
//    	return acountOperation;
//    }
}
