package com.example.LibreriaMaketPaymentMS.exceptions;

public class FailedAPICallResponseExeption extends RuntimeException {
    public FailedAPICallResponseExeption(String message) {
        super(message);
    }
}
