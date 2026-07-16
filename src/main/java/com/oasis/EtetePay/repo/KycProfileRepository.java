package com.oasis.EtetePay.repo;

import com.oasis.EtetePay.model.KYCProfile;
import com.oasis.EtetePay.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KycProfileRepository extends JpaRepository<KYCProfile, UUID> {
    Optional<KYCProfile> findByUser(User user);
}
