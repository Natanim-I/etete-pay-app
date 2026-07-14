package com.oasis.EtetePay.service;

import com.oasis.EtetePay.dto.WalletRequest;
import com.oasis.EtetePay.dto.WalletResponse;
import com.oasis.EtetePay.enums.Currency;
import com.oasis.EtetePay.enums.WalletStatus;
import com.oasis.EtetePay.exception.AccessDeniedException;
import com.oasis.EtetePay.exception.UserNotFoundException;
import com.oasis.EtetePay.exception.WalletAlreadyExistsException;
import com.oasis.EtetePay.exception.WalletNotFoundException;
import com.oasis.EtetePay.model.Wallet;
import com.oasis.EtetePay.model.auth.User;
import com.oasis.EtetePay.repo.UserRepository;
import com.oasis.EtetePay.repo.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepo;
    private final WalletRepository walletRepository;
    private final ExchangeRateService exchangeRateService;

    public BigDecimal calculateTotalBalance() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));

        List<Wallet> wallets = walletRepository.findAllByUser(user);

        BigDecimal totalBalance = BigDecimal.ZERO;

        for(Wallet wallet : wallets) {
            if (wallet.getCurrency() == Currency.USD)
                totalBalance = totalBalance.add(wallet.getBalance());
            else {
                BigDecimal rate = exchangeRateService.getCurrencyExchangeRate(wallet.getCurrency(), Currency.USD);
                totalBalance = totalBalance.add(wallet.getBalance().multiply(rate));
            }
        }
        return totalBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public List<WalletResponse> getUserWallets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        List<Wallet> wallets = walletRepository.findAllByUser(user);

        return wallets.stream()
                .map(wallet -> new WalletResponse(wallet.getWalletId(), wallet.getBalance(), wallet.getCurrency()))
                .toList();
    }

    public WalletResponse createUserWallet(WalletRequest walletRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        boolean walletExists = walletRepository.existsByUserAndCurrency(user, walletRequest.currency());
        if(walletExists){
            throw new WalletAlreadyExistsException("Wallet already exists for this currency");
        }
        Wallet wallet = new Wallet(walletRequest.currency(), BigDecimal.ZERO, user, WalletStatus.ACTIVE);
        Wallet savedWallet = walletRepository.save(wallet);
        return new WalletResponse(savedWallet.getWalletId(), savedWallet.getBalance(), savedWallet.getCurrency());
    }

    public void disableUserWallet(UUID walletId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        Wallet wallet = walletRepository.findByWalletIdAndUser(walletId, user).orElseThrow(() -> new WalletNotFoundException("Wallet not found."));
        wallet.setStatus(WalletStatus.DISABLED);
        walletRepository.save(wallet);
    }
}
