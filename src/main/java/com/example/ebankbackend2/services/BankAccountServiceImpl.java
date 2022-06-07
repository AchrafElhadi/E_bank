package com.example.ebankbackend2.services;

import com.example.ebankbackend2.entities.*;
import com.example.ebankbackend2.enums.AccountStatus;
import com.example.ebankbackend2.enums.OperationTye;
import com.example.ebankbackend2.exceptions.BalanaceNotSufficentException;
import com.example.ebankbackend2.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend2.exceptions.CustomerNotFoundException;
import com.example.ebankbackend2.repositories.AccountOperationRepo;
import com.example.ebankbackend2.repositories.BankAccountRepo;
import com.example.ebankbackend2.repositories.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
   @Autowired
    private CustomerRepo customerRepo;
   @Autowired
    private BankAccountRepo bankAccountRepo;
   @Autowired
    private AccountOperationRepo accountOperationRepo;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new customer");
        Customer c= customerRepo.save(customer);
        return  c;
    }

    @Override
    public CurrentAccount saveBankCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {


        Customer cu=customerRepo.findById(customerId).orElse(null);
        if(cu==null)
            throw new CustomerNotFoundException("Not found");
        CurrentAccount ba=new CurrentAccount();


        ba.setBalance(initialBalance);
        ba.setCreatedAt(new Date());
        ba.setId(UUID.randomUUID().toString());
        ba.setStatus(AccountStatus.CREATED);
        ba.setCustomer(cu);
        ba.setOverDraft(overDraft);
        ba=bankAccountRepo.save(ba);

        return ba;
    }

    @Override
    public SavingAccount saveBankSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer cu=customerRepo.findById(customerId).orElse(null);
        if(cu==null)
            throw new CustomerNotFoundException("Not found");
        SavingAccount ba=new SavingAccount();


        ba.setBalance(initialBalance);
        ba.setCreatedAt(new Date());
        ba.setId(UUID.randomUUID().toString());
        ba.setStatus(AccountStatus.CREATED);
        ba.setCustomer(cu);
        ba.setInterestRate(interestRate);
        ba=bankAccountRepo.save(ba);

        return ba;
    }


    @Override
    public List<Customer> listCustomer() {
        return customerRepo.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        return bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bankAccountNotfound"));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanaceNotSufficentException {
            BankAccount ba=getBankAccount(accountId);
            if(ba.getBalance()<amount)
                throw new BalanaceNotSufficentException("Balance insufisant");

        AccountOperation acp=new AccountOperation();
        acp.setType(OperationTye.DEBIT);
        acp.setAmount(amount);
        acp.setBankAccount(ba);
        acp.setOperationDate(new Date());
        accountOperationRepo.save(acp);
            ba.setBalance(ba.getBalance()-amount);
            bankAccountRepo.save(ba);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount ba=getBankAccount(accountId);

        AccountOperation acp=new AccountOperation();
        acp.setType(OperationTye.CREDIT);
        acp.setAmount(amount);
        acp.setBankAccount(ba);
        acp.setOperationDate(new Date());
        accountOperationRepo.save(acp);
        ba.setBalance(ba.getBalance()+amount);
        bankAccountRepo.save(ba);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanaceNotSufficentException {


        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }
}
