package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record RecipientResponse(
        String firstName,
        String lastName,
        String email,
        UUID walletId,
        Currency currency
) {}