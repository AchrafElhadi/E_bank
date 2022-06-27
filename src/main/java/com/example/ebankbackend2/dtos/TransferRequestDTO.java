package com.example.ebankbackend2.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String accounSource;
    private String accountDestination;
    private double amount;
    private String description;
}
