package com.example.ebankbackend2.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
