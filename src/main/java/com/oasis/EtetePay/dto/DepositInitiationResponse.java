package com.oasis.EtetePay.dto;

import java.util.UUID;

public record DepositInitiationResponse(
        UUID transactionId,
        String clientSecret
) {}