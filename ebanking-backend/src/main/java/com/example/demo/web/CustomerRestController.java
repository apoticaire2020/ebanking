package com.example.demo.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.Customer;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.service.BankAcountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {

	private BankAcountService bankAcountService;
	
	@GetMapping("/customers")
	public List<CustomerDto> customerdtos (){
		return bankAcountService.listCustomer();
	}
	
	@GetMapping("/customers/{id}")
	public CustomerDto getCustomere(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
		
		return bankAcountService.getCustomer(customerId);
	}
	
	@PostMapping("/customers")
	public CustomerDto saveCustomer(@RequestBody CustomerDto  customerDto){
		
	     return bankAcountService.saveCustomer(customerDto);
	
	}
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable(name = "id")Long cusId) {
		bankAcountService.deleteCustomer(cusId);
	}
	@PutMapping("/customers/{customerId}")
	public CustomerDto updateCustomer(@PathVariable Long customerId ,@RequestBody CustomerDto customerDto) {
		customerDto.setId(customerId);
		return bankAcountService.updateCustomer(customerDto);
	}
		
	
	
}
