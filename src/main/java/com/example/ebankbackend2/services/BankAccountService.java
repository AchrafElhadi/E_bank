package com.example.ebankbackend2.services;

import com.example.ebankbackend2.dtos.*;
import com.example.ebankbackend2.entities.BankAccount;
import com.example.ebankbackend2.entities.CurrentAccount;
import com.example.ebankbackend2.entities.Customer;
import com.example.ebankbackend2.entities.SavingAccount;
import com.example.ebankbackend2.exceptions.BalanaceNotSufficentException;
import com.example.ebankbackend2.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend2.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customer);

    CurrentBankAccountDTO saveBankCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveBankSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanaceNotSufficentException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanaceNotSufficentException;

    List<BankAccountDTO>bankAccountList();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO cust);

    void deleteCustomer(Long cust);

    List<AccountOperationDTO> accountHistory(String accid);


    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String key);
}
