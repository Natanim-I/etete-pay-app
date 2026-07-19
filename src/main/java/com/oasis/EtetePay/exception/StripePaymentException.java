package com.oasis.EtetePay.exception;

public class StripePaymentException extends RuntimeException {
    public StripePaymentException(String message) {
        super(message);
    }
}
