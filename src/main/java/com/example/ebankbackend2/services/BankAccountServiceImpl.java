package com.example.ebankbackend2.services;

import com.example.ebankbackend2.dtos.*;
import com.example.ebankbackend2.entities.*;
import com.example.ebankbackend2.enums.AccountStatus;
import com.example.ebankbackend2.enums.OperationTye;
import com.example.ebankbackend2.exceptions.BalanaceNotSufficentException;
import com.example.ebankbackend2.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend2.exceptions.CustomerNotFoundException;
import com.example.ebankbackend2.mappers.BankAccountMapperImpl;
import com.example.ebankbackend2.repositories.AccountOperationRepo;
import com.example.ebankbackend2.repositories.BankAccountRepo;
import com.example.ebankbackend2.repositories.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


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
    @Autowired
    private BankAccountMapperImpl bankAccountMapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("saving new customer");
        return bankAccountMapper.fromCustomer(customerRepo.save(bankAccountMapper.fromCustomerDTO(customer)));

     }

    @Override
    public CurrentBankAccountDTO saveBankCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {


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

        return bankAccountMapper.fromcurrent( ba);
    }

    @Override
    public SavingBankAccountDTO saveBankSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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

        return bankAccountMapper.fromsaving( ba);
    }


    @Override
    public List<CustomerDTO> listCustomer() {
        List<CustomerDTO> cuslst;

       cuslst= customerRepo.findAll().stream().map(cu-> bankAccountMapper.fromCustomer(cu)).collect(Collectors.toList());

//        customerRepo.findAll().forEach(v->{
//            lcdto.add(bankAccountMapper.fromCustomer(v));
//        });
        return cuslst;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount b= bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bankAccountNotfound"));
    if(b instanceof CurrentAccount)
        return bankAccountMapper.fromcurrent((CurrentAccount) b);
    else
        return bankAccountMapper.fromsaving((SavingAccount) b);

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanaceNotSufficentException {
        BankAccount ba= bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bankAccountNotfound"));
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
        BankAccount ba= bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bankAccountNotfound"));

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
    @Override
    public List<BankAccountDTO>bankAccountList(){
        List<BankAccountDTO> b= bankAccountRepo.findAll().stream().map(v->{
             if(v instanceof SavingAccount)
                 return bankAccountMapper.fromsaving((SavingAccount) v);
             else
                 return bankAccountMapper.fromcurrent((CurrentAccount) v);

         }).collect(Collectors.toList());
        return b;
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException
    {
        CustomerDTO cudto;
        cudto=bankAccountMapper.fromCustomer(customerRepo.findById(id).orElseThrow(()->new CustomerNotFoundException("Not found")));
        return cudto;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO cust)
    {
        return bankAccountMapper.fromCustomer(customerRepo.save(bankAccountMapper.fromCustomerDTO(cust)));
    }
    @Override
    public void deleteCustomer(Long cust) {
        customerRepo.deleteById(cust);

    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accid)
    {
        List<AccountOperationDTO> acodt= accountOperationRepo.findByBankAccountId(accid).stream().map(v->
            bankAccountMapper.fromAccountOperation(v)).
                collect(Collectors.toList());

        return acodt;
    }
    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Not found"));
        Page <AccountOperation>accountOperations=accountOperationRepo.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.getContent().stream().map(v->bankAccountMapper.fromAccountOperation(v)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTO(accountOperationDTOS);
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;

    }

    @Override
    public List<CustomerDTO> searchCustomers(String key) {

        List<CustomerDTO> dto=new ArrayList<>();
        customerRepo.findByNameContains(key).forEach(v->
        {
            dto.add(bankAccountMapper.fromCustomer(v));
        });
        return dto;
    }
}
