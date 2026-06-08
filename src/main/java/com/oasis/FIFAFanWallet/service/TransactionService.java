package com.oasis.FIFAFanWallet.service;

import com.oasis.FIFAFanWallet.dto.TransactionRequest;
import com.oasis.FIFAFanWallet.dto.TransactionResponse;
import com.oasis.FIFAFanWallet.enums.TransactionStatus;
import com.oasis.FIFAFanWallet.enums.TransactionType;
import com.oasis.FIFAFanWallet.enums.WalletStatus;
import com.oasis.FIFAFanWallet.exception.AccessDeniedException;
import com.oasis.FIFAFanWallet.exception.IllegalStateException;
import com.oasis.FIFAFanWallet.exception.WalletNotFoundException;
import com.oasis.FIFAFanWallet.model.Transaction;
import com.oasis.FIFAFanWallet.model.Wallet;
import com.oasis.FIFAFanWallet.repo.TransactionRepository;
import com.oasis.FIFAFanWallet.repo.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponse deposit(UUID walletId, TransactionRequest transactionRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found!"));
        if(!wallet.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("Wallet doesn't belong to this user.");
        }

        if(wallet.getStatus() == WalletStatus.DISABLED){
            throw new IllegalStateException("Wallet is Disabled.");
        }

        wallet.setBalance(wallet.getBalance().add(transactionRequest.amount()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.amount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setWallet(wallet);
        transaction.setStatus(TransactionStatus.SUCCESS);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getTransactionId(),
                savedTransaction.getWallet().getWalletId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getStatus(),
                savedTransaction.getCreatedAt()
        );
    }

    @Transactional
    public TransactionResponse withdraw(UUID walletId, TransactionRequest transactionRequest){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found!"));
        if(!wallet.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("Wallet doesn't belong to this user.");
        }

        if(wallet.getStatus() == WalletStatus.DISABLED){
            throw new IllegalStateException("Wallet is Disabled!");
        }

        if(wallet.getBalance().compareTo(transactionRequest.amount()) < 0){
            throw new IllegalStateException("Insufficient balance.");
        }

        wallet.setBalance(wallet.getBalance().subtract(transactionRequest.amount()));
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.amount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setWallet(wallet);
        transaction.setStatus(TransactionStatus.SUCCESS);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getTransactionId(),
                savedTransaction.getWallet().getWalletId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getStatus(),
                savedTransaction.getCreatedAt()
        );
    }
}
