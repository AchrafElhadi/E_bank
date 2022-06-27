package com.example.ebankbackend2;

import com.example.ebankbackend2.dtos.BankAccountDTO;
import com.example.ebankbackend2.dtos.CurrentBankAccountDTO;
import com.example.ebankbackend2.dtos.CustomerDTO;
import com.example.ebankbackend2.dtos.SavingBankAccountDTO;
import com.example.ebankbackend2.entities.*;
import com.example.ebankbackend2.enums.AccountStatus;
import com.example.ebankbackend2.enums.OperationTye;
import com.example.ebankbackend2.exceptions.BalanaceNotSufficentException;
import com.example.ebankbackend2.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend2.exceptions.CustomerNotFoundException;
import com.example.ebankbackend2.repositories.AccountOperationRepo;
import com.example.ebankbackend2.repositories.BankAccountRepo;
import com.example.ebankbackend2.repositories.CustomerRepo;
import com.example.ebankbackend2.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackend2Application {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackend2Application.class, args);
    }
    //@Bean
    CommandLineRunner startm(BankAccountService bankAccountService) {
        return args ->
        {
            Stream.of("iniesta","xavi","Busquests").forEach(name->
            {
                CustomerDTO c= new CustomerDTO();
                c.setName(name);
                c.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer( c);
            });
            bankAccountService.listCustomer().forEach(cu->{
                try {
                    bankAccountService.saveBankCurrentAccount(Math.random()*90000,9000,cu.getId());
                    bankAccountService.saveBankSavingAccount(Math.random()*9000,300,cu.getId());
                    List<BankAccountDTO> bankAccounts=bankAccountService.bankAccountList();
                    for(BankAccountDTO bankAccount:bankAccounts)
                    {
                        String acctid;
                        if(bankAccount instanceof SavingBankAccountDTO)
                        {
                            acctid=(((SavingBankAccountDTO) bankAccount).getId());
                        }
                        else
                            acctid=(((CurrentBankAccountDTO) bankAccount).getId());

                        bankAccountService.credit(acctid,1000+Math.random()*12000,"Credit");
                        bankAccountService.debit(acctid,1000+Math.random()*9000,"Debit");
                    }

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
                catch (BankAccountNotFoundException | BalanaceNotSufficentException e) {
                    e.printStackTrace();
                }
            });
        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepo custRepo, BankAccountRepo bankRepo, AccountOperationRepo accountOperRepo)
    {
        return args->
        {
            Stream.of( "achraf","El hadi","chi hed").forEach(name->
            {
                Customer cust=new Customer();
                cust.setEmail(name+"@gmail.com");
                cust.setName(name);
                custRepo.save(cust);
            });

            custRepo.findAll().forEach(v->
            {
                CurrentAccount Ca=new CurrentAccount();
                Ca.setId(UUID.randomUUID().toString());
                Ca.setCreatedAt(new Date());
                Ca.setBalance(455);
                Ca.setStatus(AccountStatus.CREATED);
                Ca.setCustomer(v);
                Ca.setOverDraft(345);
                bankRepo.save(Ca);
            });
            custRepo.findAll().forEach(v->
            {
                SavingAccount Sa=new SavingAccount();
                Sa.setId(UUID.randomUUID().toString());
                Sa.setCreatedAt(new Date());
                Sa.setBalance(455);
                Sa.setStatus(AccountStatus.CREATED);
                Sa.setCustomer(v);
                Sa.setInterestRate(34);
                bankRepo.save(Sa);
            });

            bankRepo.findAll().forEach(bank->
                    {
                        for (int i = 0; i < 5; i++) {
                            AccountOperation ao = new AccountOperation();
                            ao.setAmount(Math.random() * 1000);
                            ao.setBankAccount(bank);
                            ao.setOperationDate(new Date());
                            ao.setType(Math.random()>0.5? OperationTye.DEBIT:OperationTye.CREDIT);
                            accountOperRepo.save(ao);
                        }
                    }

            );


        };

    }

}


