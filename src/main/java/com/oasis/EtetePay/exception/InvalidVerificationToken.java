package com.oasis.EtetePay.exception;

public class InvalidVerificationToken extends RuntimeException {
    public InvalidVerificationToken(String message) {
        super(message);
    }
}
