package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.enums.Currency;
import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(UUID walletId, BigDecimal balance, Currency currency) {
}
