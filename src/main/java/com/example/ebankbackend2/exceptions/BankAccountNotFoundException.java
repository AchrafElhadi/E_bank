package com.example.ebankbackend2.exceptions;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String msg) {
        super(msg);
    }
}
