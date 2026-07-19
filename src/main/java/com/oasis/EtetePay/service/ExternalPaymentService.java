package com.oasis.EtetePay.service;

import com.oasis.EtetePay.enums.TransactionStatus;
import com.oasis.EtetePay.exception.StripePaymentException;
import com.oasis.EtetePay.model.Transaction;
import com.oasis.EtetePay.model.Wallet;
import com.oasis.EtetePay.repo.TransactionRepository;
import com.oasis.EtetePay.repo.WalletRepository;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalPaymentService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void handlePaymentSuccess(Event event){

        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

        Transaction transaction = transactionRepository.findByPaymentIntentId(paymentIntent.getId())
                .orElseThrow(() -> new StripePaymentException("Payment Intent ID not found in the event."));

        if(transaction.getStatus() == TransactionStatus.SUCCESS){
            return;
        }

        Wallet wallet = transaction.getWallet();
        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
    }
}
