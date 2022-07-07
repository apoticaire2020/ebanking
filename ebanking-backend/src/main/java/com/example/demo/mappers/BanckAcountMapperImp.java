package com.example.demo.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.Customer;

@Service
public class BanckAcountMapperImp {

	public CustomerDto fromCustomer(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		BeanUtils.copyProperties(customer, customerDto);
		//customerDto.setId(customer.getId());
		//customerDto.setName(customer.getName());
		//customerDto.setEmail(customer.getEmail());
		return customerDto;
	}
	public Customer fromCustomerDto(CustomerDto customerdto) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerdto, customer);
		return customer;
	}
}
