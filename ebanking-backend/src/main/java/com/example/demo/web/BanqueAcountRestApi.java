package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.AccountOperationDto;
import com.example.demo.dtos.BankAccountDto;
import com.example.demo.exceptions.BankAcountNotFoundException;
import com.example.demo.service.BankAccountService;



@RestController
public class BanqueAcountRestApi {
	
	
	private BankAccountService acountService;
	
	public BanqueAcountRestApi(BankAccountService acountService) {
		this.acountService= acountService;
	}
	
	@GetMapping("/accounts/{acountId}")
	public BankAccountDto getBankAcount(@PathVariable String acountId) throws BankAcountNotFoundException {
		
	return	acountService.getBankAcount(acountId);
		
	}
	@GetMapping("/accounts")
	public List<BankAccountDto> getbankAcountDtos(){
		return acountService.bankAcountList();
	}
	
//	@GetMapping("/accounts/{accountId}/operations")
//	public List<AccountOperationDto> getAcountOperationDtos(@PathVariable String accountId){
//		return acountService.accountHistorique(accountId);
//	}
//	

}
