package com.oasis.EtetePay.helpers;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Helper {
    public long toStripeAmount(BigDecimal amount) {
        return amount.movePointRight(2).longValueExact();
    }
}
