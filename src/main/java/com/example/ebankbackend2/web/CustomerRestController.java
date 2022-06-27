package com.example.ebankbackend2.web;

import com.example.ebankbackend2.dtos.CustomerDTO;
import com.example.ebankbackend2.entities.Customer;
import com.example.ebankbackend2.exceptions.CustomerNotFoundException;
import com.example.ebankbackend2.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers()
    {
        return bankAccountService.listCustomer();
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO customers(@PathVariable(name="id") Long cusId) throws CustomerNotFoundException {

        return bankAccountService.getCustomer(cusId);
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchcustomers(@RequestParam(name="keyword",defaultValue = "") String key) {
        return bankAccountService.searchCustomers(key);
    }
    @PostMapping("/customers")
    public CustomerDTO savecustomers(@RequestBody CustomerDTO cust)  {
        return bankAccountService.saveCustomer(cust);
    }

    @PutMapping("/customers")
    public CustomerDTO updatecustomers( @RequestBody CustomerDTO cust){

        return bankAccountService.updateCustomer(cust);
    }
    
    @DeleteMapping("/customers/{id}")
    public void deletecustomer(@PathVariable(name="id") Long id)
    {
        bankAccountService.deleteCustomer(id);
    }
     
}
