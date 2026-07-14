package com.oasis.EtetePay.model;

import com.oasis.EtetePay.enums.BudgetCategory;
import com.oasis.EtetePay.enums.BudgetPeriod;
import com.oasis.EtetePay.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID budgetId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    @Enumerated(EnumType.STRING)
    private BudgetPeriod type;
    @Enumerated(EnumType.STRING)
    private BudgetCategory category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
