package com.example.ebankbackend2.mappers;

import com.example.ebankbackend2.dtos.AccountOperationDTO;
import com.example.ebankbackend2.dtos.CurrentBankAccountDTO;
import com.example.ebankbackend2.dtos.CustomerDTO;
import com.example.ebankbackend2.dtos.SavingBankAccountDTO;
import com.example.ebankbackend2.entities.AccountOperation;
import com.example.ebankbackend2.entities.CurrentAccount;
import com.example.ebankbackend2.entities.Customer;
import com.example.ebankbackend2.entities.SavingAccount;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
//        customerDTO.setId(customer.getId());
//        customerDTO.setEmail(customer.getEmail());
//        customerDTO.setName(customer.getName());
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO)
    {
        Customer cu=new Customer();
        BeanUtils.copyProperties(customerDTO,cu);
        return cu;
    }
    public SavingBankAccountDTO fromsaving(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO=new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType("SavingAccount");
        return savingBankAccountDTO;
    }
    public SavingAccount fromsavingDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentBankAccountDTO fromcurrent(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType("CurrentAccount");
        return currentBankAccountDTO;
    }
    public CurrentAccount fromcurrentDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation)
    {
        AccountOperationDTO accountOperationDTO= new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }

}
