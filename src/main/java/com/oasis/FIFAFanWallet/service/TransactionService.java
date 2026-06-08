package com.oasis.FIFAFanWallet.service;

import com.oasis.FIFAFanWallet.dto.TransactionRequest;
import com.oasis.FIFAFanWallet.dto.TransactionResponse;
import com.oasis.FIFAFanWallet.dto.TransferResponse;
import com.oasis.FIFAFanWallet.enums.TransactionStatus;
import com.oasis.FIFAFanWallet.enums.TransactionType;
import com.oasis.FIFAFanWallet.enums.WalletStatus;
import com.oasis.FIFAFanWallet.exception.*;
import com.oasis.FIFAFanWallet.exception.IllegalArgumentException;
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
            throw new WalletIsDisabledException("Wallet is Disabled!");
        }

        if(wallet.getBalance().compareTo(transactionRequest.amount()) < 0){
            throw new InsufficientFundsException("Insufficient balance.");
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

    @Transactional
    public TransferResponse transfer(UUID senderId, UUID receiverId, TransactionRequest transactionRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(senderId.equals(receiverId)){
            throw new IllegalArgumentException("Transfer to the same account is not supported.");
        }

        Wallet senderWallet = walletRepository.findById(senderId).orElseThrow(() -> new WalletNotFoundException("Wallet not found."));
        Wallet receiverWallet = walletRepository.findById(receiverId).orElseThrow(() -> new WalletNotFoundException("Wallet not found."));

        if(!senderWallet.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("Wallet doesn't belong to this user.");
        }
        if(senderWallet.getStatus() == WalletStatus.DISABLED || receiverWallet.getStatus() == WalletStatus.DISABLED){
            throw new WalletIsDisabledException("Wallet is disabled.");
        }

        if(!senderWallet.getCurrency().equals(receiverWallet.getCurrency())){
            throw new CurrencyMismatchException("Cross-currency transfers are not supported.");
        }

        if(senderWallet.getBalance().compareTo(transactionRequest.amount()) < 0){
            throw new InsufficientFundsException("Insufficient balance.");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(transactionRequest.amount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(transactionRequest.amount()));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        Transaction sentTransaction = new Transaction();
        sentTransaction.setAmount(transactionRequest.amount());
        sentTransaction.setType(TransactionType.TRANSFER_OUT);
        sentTransaction.setWallet(senderWallet);
        sentTransaction.setStatus(TransactionStatus.SUCCESS);

        Transaction receivedTransaction = new Transaction();
        receivedTransaction.setAmount(transactionRequest.amount());
        receivedTransaction.setType(TransactionType.TRANSFER_IN);
        receivedTransaction.setWallet(receiverWallet);
        receivedTransaction.setStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(sentTransaction);
        transactionRepository.save(receivedTransaction);

        return new TransferResponse(
                sentTransaction.getTransactionId(),
                senderWallet.getWalletId(),
                receiverWallet.getWalletId(),
                transactionRequest.amount(),
                sentTransaction.getType(),
                sentTransaction.getStatus(),
                sentTransaction.getCreatedAt()
        );
    }
}
