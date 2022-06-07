package com.example.ebankbackend2.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("CA")
@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class CurrentAccount extends BankAccount{
    private double overDraft;
}
