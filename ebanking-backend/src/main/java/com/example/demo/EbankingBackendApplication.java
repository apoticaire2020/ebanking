package com.example.demo;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.entities.AcountOperation;
import com.example.demo.entities.BankAcount;
import com.example.demo.entities.CurentAcount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAcount;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BankAcountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.repo.AcounrOperationRepo;
import com.example.demo.repo.BankAcounrRepo;
import com.example.demo.repo.CustomerRepo;
import com.example.demo.service.BankAcountService;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	
	
	@Bean
	CommandLineRunner commandLineRunner(BankAcountService bankAcountService) {
		return args->{
			Customer c = new Customer();
			Stream.of("hassan","fouad","said","ali").forEach(name->{
				c.setName(name);
				c.setEmail(name+ "@yahoo.fr");
				
				bankAcountService.saveCustomer(c);
			});
			bankAcountService.listCustomer().forEach(cus->
			{
			{try {
			  bankAcountService.saveCurentBankAcount(Math.random()*100000, c.getId(), 1500);
			  bankAcountService.saveSavingBankAcount(Math.random()*10000 , c.getId(),8.5);	
			//  bankAcountService.bankAcountList()
			} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}}
			});	
		};
		
	}
	
	
	}
