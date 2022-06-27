package com.example.ebankbackend2.repositories;

import com.example.ebankbackend2.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
    List<Customer> findByNameContains(String keyword);
}
