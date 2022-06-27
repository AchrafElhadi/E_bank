package com.example.ebankbackend2.exceptions;

import java.util.function.Supplier;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String msg) {
        super(msg);
    }
}
