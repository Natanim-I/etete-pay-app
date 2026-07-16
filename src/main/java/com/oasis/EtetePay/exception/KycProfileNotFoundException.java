package com.oasis.EtetePay.exception;

public class KycProfileNotFoundException extends RuntimeException {
    public KycProfileNotFoundException(String message) {
        super(message);
    }
}
