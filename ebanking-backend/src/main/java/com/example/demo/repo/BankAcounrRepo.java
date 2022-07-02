package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.BankAcount;


public interface BankAcounrRepo extends JpaRepository<BankAcount,String>{

}
