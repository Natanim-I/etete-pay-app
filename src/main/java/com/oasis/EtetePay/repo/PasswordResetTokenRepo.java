package com.oasis.EtetePay.repo;

import com.oasis.EtetePay.model.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String resetToken);
}
