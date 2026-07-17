package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.enums.KYCStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record KycResponse(UUID kycId, KYCStatus status, LocalDateTime submittedAt) {
}
