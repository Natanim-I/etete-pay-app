package com.oasis.EtetePay.exception;

public class WalletIsDisabledException extends RuntimeException {
    public WalletIsDisabledException(String message) {
        super(message);
    }
}
