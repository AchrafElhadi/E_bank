package com.example.ebankbackend2.dtos;

import com.example.ebankbackend2.entities.BankAccount;
import com.example.ebankbackend2.enums.OperationTye;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
public class AccountOperationDTO {
        private Long id;
        private Date operationDate;
        private double amount;
         private OperationTye type;

}
