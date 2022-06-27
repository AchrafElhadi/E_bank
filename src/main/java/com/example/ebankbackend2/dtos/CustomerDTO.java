package com.example.ebankbackend2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
public class CustomerDTO{
    private Long id;
    private String name;
    private String email;
}
