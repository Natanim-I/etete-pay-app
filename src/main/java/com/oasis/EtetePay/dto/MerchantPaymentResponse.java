package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.enums.BudgetCategory;
import com.oasis.EtetePay.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MerchantPaymentResponse(
        UUID paymentId,
        UUID userId,
        UUID walletId,
        UUID transactionId,
        BigDecimal amount,
        BudgetCategory budgetCategory,
        String merchantName,
        String description,
        PaymentStatus status,
        LocalDateTime createdAt
) {
}
