package com.example.demo.web;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.BankAcountService;

import lombok.AllArgsConstructor;

@RestController

public class BanqueAcountRestApi {
	
	private BankAcountService acountService;
	
	public BanqueAcountRestApi(BankAcountService acountService) {
		this.acountService= acountService;
	}
	
	

}
