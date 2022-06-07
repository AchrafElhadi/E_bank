package com.example.ebankbackend2;

import com.example.ebankbackend2.entities.*;
import com.example.ebankbackend2.enums.AccountStatus;
import com.example.ebankbackend2.enums.OperationTye;
import com.example.ebankbackend2.repositories.AccountOperationRepo;
import com.example.ebankbackend2.repositories.BankAccountRepo;
import com.example.ebankbackend2.repositories.CustomerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackend2Application {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackend2Application.class, args);
    }
    @Bean
    CommandLineRunner startm(CustomerRepo custRepo, BankAccountRepo bankRepo, AccountOperationRepo accountOperRepo) {
        return args ->
        {
            BankAccount bank= bankRepo.findById("166baaeb-fd7f-4c58-b7fa-8be2d133e1e9").orElse(null);
            if(bank!=null) {
                System.out.println("Balance "+bank.getBalance() + " customer " + bank.getCustomer().getName() + " status " + bank.getStatus());
                if (bank instanceof CurrentAccount)
                    System.out.println(((CurrentAccount) bank).getOverDraft());
                else
                    System.out.println(((SavingAccount) bank).getInterestRate());
            }
            else
                System.out.println("nulllll");
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


