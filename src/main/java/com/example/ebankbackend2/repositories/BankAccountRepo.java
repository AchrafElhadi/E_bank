package com.example.ebankbackend2.repositories;

import com.example.ebankbackend2.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount,String > {
}
