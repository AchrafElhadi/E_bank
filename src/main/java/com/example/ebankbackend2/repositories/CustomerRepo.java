package com.example.ebankbackend2.repositories;

import com.example.ebankbackend2.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
