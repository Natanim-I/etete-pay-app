package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.enums.TransactionStatus;
import com.oasis.EtetePay.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(
        UUID id,
        UUID senderId,
        UUID receiverId,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        LocalDateTime createdAt
) {}
