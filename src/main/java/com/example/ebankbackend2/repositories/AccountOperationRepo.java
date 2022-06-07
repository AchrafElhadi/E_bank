package com.example.ebankbackend2.repositories;

import com.example.ebankbackend2.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepo extends JpaRepository<AccountOperation,Long> {
}
