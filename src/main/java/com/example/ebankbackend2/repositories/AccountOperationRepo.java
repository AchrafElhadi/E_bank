package com.example.ebankbackend2.repositories;

import com.example.ebankbackend2.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepo extends JpaRepository<AccountOperation,Long> {
  List<AccountOperation> findByBankAccountId(String accountId );
  Page<AccountOperation> findByBankAccountId(String accountId, Pageable pageable);
}
