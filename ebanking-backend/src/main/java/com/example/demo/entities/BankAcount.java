package com.example.demo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.demo.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 4 , name = "TYPE")
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAcount {

	@Id 
	private String id;
	private double balance;
	private Date createAt;
	@Enumerated(EnumType.STRING)
	private AccountStatus status ;
	
	@ManyToOne
	private Customer customer;
	
	@OneToMany(mappedBy = "bankAcount")
	private List<AcountOperation> acountOperations;
}
