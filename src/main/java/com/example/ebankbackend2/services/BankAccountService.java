package com.example.ebankbackend2.services;

import com.example.ebankbackend2.entities.BankAccount;
import com.example.ebankbackend2.entities.CurrentAccount;
import com.example.ebankbackend2.entities.Customer;
import com.example.ebankbackend2.entities.SavingAccount;
import com.example.ebankbackend2.exceptions.BalanaceNotSufficentException;
import com.example.ebankbackend2.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend2.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveBankCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveBankSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomer();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanaceNotSufficentException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanaceNotSufficentException;

}
