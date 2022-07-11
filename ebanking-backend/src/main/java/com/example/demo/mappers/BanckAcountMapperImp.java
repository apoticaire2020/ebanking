package com.example.demo.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.CurentBankAcountDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.SavingBankAcountDto;
import com.example.demo.entities.CurentAcount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAcount;

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
	
	public CurentBankAcountDto	fromCurentAcount(CurentAcount acount) {
		CurentBankAcountDto acountDto = new CurentBankAcountDto();
		BeanUtils.copyProperties(acount, acountDto);
		acountDto.setCustomerDto(fromCustomer(acount.getCustomer()));
		      return acountDto;
	}
	public CurentAcount	fromCurentAcountDto(CurentBankAcountDto acount) {
		CurentAcount curentAcount = new CurentAcount();
		BeanUtils.copyProperties(acount, curentAcount);
		curentAcount.setCustomer(fromCustomerDto(acount.getCustomerDto()));
		      return curentAcount;
	}
	public SavingBankAcountDto fromSavingBankAcount(SavingAcount savingAcount) {
		SavingBankAcountDto savingBankAcountDto = new SavingBankAcountDto();
		BeanUtils.copyProperties(savingAcount, savingBankAcountDto);
		savingBankAcountDto.setCustomerDto(fromCustomer(savingAcount.getCustomer()));
		    return savingBankAcountDto;
		
	}
	public SavingAcount fromSavingBankAcountDto(SavingBankAcountDto savingBankAcountDto) {
		SavingAcount savingAcount = new SavingAcount();
		BeanUtils.copyProperties(savingBankAcountDto, savingAcount);
		savingAcount.setCustomer(fromCustomerDto(savingBankAcountDto.getCustomerDto()));
		    return savingAcount;
		
	}
}
