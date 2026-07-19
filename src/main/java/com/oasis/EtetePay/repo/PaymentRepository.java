package com.oasis.EtetePay.repo;

import com.oasis.EtetePay.model.MerchantPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<MerchantPayment, UUID> {
}
