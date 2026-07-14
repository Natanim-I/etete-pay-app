package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.enums.BudgetCategory;
import com.oasis.EtetePay.enums.BudgetPeriod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BudgetResponse(
        UUID budgetId,
        BigDecimal limitAmount,
        BigDecimal spentAmount,
        BudgetPeriod type,
        BudgetCategory category,
        LocalDateTime startDate,
        LocalDateTime endDate
) {}
