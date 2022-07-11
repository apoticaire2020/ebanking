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

import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.BankAcountDto;
import com.example.demo.entities.AcountOperation;
import com.example.demo.entities.BankAcount;
import com.example.demo.entities.CurentAcount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAcount;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
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
			
			Stream.of("hassan","fouad","said","ali").forEach(name->{
				CustomerDto c = new CustomerDto();
				c.setName(name);
				c.setEmail(name+ "@yahoo.fr");
				bankAcountService.saveCustomer(c);
			});
			
			bankAcountService.listCustomer().forEach(cust->
			
			{		
			  try {
				bankAcountService.saveCurentBankAcount(Math.random()*100000, cust.getId(), 1500);
			    bankAcountService.saveSavingBankAcount(Math.random()*10000 , cust.getId(),8.5);	
			    List<BankAcountDto> bankAcounts= bankAcountService.bankAcountList();
			     for(BankAcountDto ba:bankAcounts) {
				  for (int i = 0; i < 10; i++) {
					bankAcountService.credit(ba.getId(), Math.random()*2000+2000, "credit");
					bankAcountService.debit(ba.getId(), Math.random()*1000+500, "debit");
				 
				  }
			}
			    } catch (CustomerNotFoundException e) {
									e.printStackTrace();
				} catch (BankAcountNotFoundException |BalanceNotSufficientException e) {
				    			e.printStackTrace();
			    } 
		});
		
	};
	
	
	}
	
}
