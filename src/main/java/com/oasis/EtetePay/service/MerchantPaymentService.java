package com.oasis.EtetePay.service;

import com.oasis.EtetePay.dto.MerchantPaymentRequest;
import com.oasis.EtetePay.dto.MerchantPaymentResponse;
import com.oasis.EtetePay.enums.PaymentStatus;
import com.oasis.EtetePay.enums.TransactionStatus;
import com.oasis.EtetePay.enums.TransactionType;
import com.oasis.EtetePay.exception.BudgetNotFoundException;
import com.oasis.EtetePay.exception.InsufficientFundsException;
import com.oasis.EtetePay.exception.UserNotFoundException;
import com.oasis.EtetePay.exception.WalletNotFoundException;
import com.oasis.EtetePay.model.Budget;
import com.oasis.EtetePay.model.MerchantPayment;
import com.oasis.EtetePay.model.Transaction;
import com.oasis.EtetePay.model.Wallet;
import com.oasis.EtetePay.model.auth.User;
import com.oasis.EtetePay.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MerchantPaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final ExchangeRateService exchangeRateService;

    @Transactional
    public MerchantPaymentResponse makePayment(MerchantPaymentRequest merchantPaymentRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));

        Wallet wallet = walletRepository.findByWalletIdAndUser(merchantPaymentRequest.walletId(), user).orElseThrow(() -> new WalletNotFoundException("Wallet associated with this user not found."));

        if(wallet.getBalance().compareTo(merchantPaymentRequest.amount()) < 0){
            throw new InsufficientFundsException("Insufficient funds.");
        }

        wallet.setBalance(wallet.getBalance().subtract(merchantPaymentRequest.amount()));

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(merchantPaymentRequest.amount());
        transaction.setType(TransactionType.PAYMENT);
        transaction.setCategory(merchantPaymentRequest.budgetCategory());
        transaction.setStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(transaction);

        Budget budget = budgetRepository.findBudgets(
                user.getUserId(),
                merchantPaymentRequest.budgetCategory(),
                LocalDateTime.now()
        ).orElseThrow(() -> new BudgetNotFoundException("Budget not found."));

        if(budget.getCurrency() == wallet.getCurrency()){
            budget.setSpentAmount(budget.getSpentAmount().add(merchantPaymentRequest.amount()).setScale(2, RoundingMode.HALF_UP));
        } else {
            BigDecimal rate = exchangeRateService.getCurrencyExchangeRate(wallet.getCurrency(), budget.getCurrency());
            BigDecimal paymentAmount = merchantPaymentRequest.amount().multiply(rate);
            budget.setSpentAmount(budget.getSpentAmount().add(paymentAmount).setScale(2, RoundingMode.HALF_UP));
        }

        MerchantPayment savedPayment = new MerchantPayment();
        savedPayment.setTransaction(transaction);
        savedPayment.setUserId(user.getUserId());
        savedPayment.setWalletId(wallet.getWalletId());
        savedPayment.setAmount(merchantPaymentRequest.amount());
        savedPayment.setBudgetCategory(merchantPaymentRequest.budgetCategory());
        savedPayment.setMerchantName(merchantPaymentRequest.merchantName());
        savedPayment.setDescription(merchantPaymentRequest.description());
        savedPayment.setStatus(PaymentStatus.COMPLETED);


        return new MerchantPaymentResponse(
                savedPayment.getPaymentId(),
                user.getUserId(),
                wallet.getWalletId(),
                transaction.getTransactionId(),
                savedPayment.getAmount(),
                savedPayment.getBudgetCategory(),
                savedPayment.getMerchantName(),
                savedPayment.getDescription(),
                savedPayment.getStatus(),
                savedPayment.getCreatedAt()
        );
    }
}
