package com.example.ebankbackend2.web;

import com.example.ebankbackend2.dtos.*;
import com.example.ebankbackend2.exceptions.BalanaceNotSufficentException;
import com.example.ebankbackend2.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend2.services.BankAccountService;
import com.example.ebankbackend2.services.BankAccountServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankaccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/bankacc")
    public List<BankAccountDTO> listaccounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping("/bankacc/{idacc}")
    public BankAccountDTO accountById(@PathVariable(name = "idacc") String idacc) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(idacc);
    }

    @GetMapping("/AccountOperation/{idacc}")
    public List<AccountOperationDTO> Accountopbyidacc(@PathVariable(name = "idacc") String idacc){
        return bankAccountService.accountHistory(idacc);
    }

    @GetMapping("/AccountOperation/{idacc}/pagesOperations")
    public AccountHistoryDTO getAccounthist(@PathVariable(name = "idacc") String idacc, @RequestParam(name="page",defaultValue = "0")
                                                    int page, @RequestParam(name="size",defaultValue = "5" ) int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(idacc,page,size);
    }


    @PostMapping("/AccountOperation/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanaceNotSufficentException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/AccountOperation/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanaceNotSufficentException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/AccountOperation/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanaceNotSufficentException {
        this.bankAccountService.transfer(transferRequestDTO.getAccounSource(),transferRequestDTO.getAccountDestination(),transferRequestDTO.getAmount());
    }





}
