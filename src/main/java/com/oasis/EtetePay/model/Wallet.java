package com.oasis.EtetePay.model;

import com.oasis.EtetePay.enums.Currency;
import com.oasis.EtetePay.enums.WalletStatus;
import com.oasis.EtetePay.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;
    private Currency currency;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private WalletStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @Version
    private Long version;

    public Wallet(Currency currency, BigDecimal balance, User user, WalletStatus status){
        this.currency = currency;
        this.balance = balance;
        this.user = user;
        this.status = status;
    }
}
