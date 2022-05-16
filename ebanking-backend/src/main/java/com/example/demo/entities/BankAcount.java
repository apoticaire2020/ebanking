package com.example.demo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.demo.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAcount {

	@Id 
	private String id;
	private double balance;
	private Date createAt;
	
	private AccountStatus status ;
	
	@ManyToOne
	private Customer customer;
	@OneToMany(mappedBy = "bankAcount")
	private List<AcountOperation> acountOperations;
}
