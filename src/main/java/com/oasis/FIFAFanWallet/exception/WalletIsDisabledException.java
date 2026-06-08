package com.oasis.FIFAFanWallet.exception;

public class WalletIsDisabledException extends RuntimeException {
    public WalletIsDisabledException(String message) {
        super(message);
    }
}
