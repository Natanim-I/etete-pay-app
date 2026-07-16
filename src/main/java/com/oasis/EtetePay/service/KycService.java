package com.oasis.EtetePay.service;

import com.oasis.EtetePay.dto.KycRequest;
import com.oasis.EtetePay.dto.KycResponse;
import com.oasis.EtetePay.enums.KYCStatus;
import com.oasis.EtetePay.exception.KycProfileNotFoundException;
import com.oasis.EtetePay.exception.UserNotFoundException;
import com.oasis.EtetePay.model.KYCProfile;
import com.oasis.EtetePay.model.auth.User;
import com.oasis.EtetePay.repo.KycProfileRepository;
import com.oasis.EtetePay.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycProfileRepository kycProfileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public KycResponse processKyc(KycRequest kycRequest, MultipartFile idImage, MultipartFile selfieImage) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        KYCProfile kycProfile = kycProfileRepository.findByUser(user).orElseThrow(() -> new KycProfileNotFoundException("Kyc profile not found."));

        kycProfile.setFaydaNumber(kycRequest.faydaNumber());
        kycProfile.setFirstName(kycRequest.firstName());
        kycProfile.setMiddleName(kycRequest.middleName());
        kycProfile.setLastName(kycRequest.lastName());
        kycProfile.setDateOfBirth(kycRequest.dateOfBirth());
        kycProfile.setPhoneNumber(kycRequest.phoneNumber());
        kycProfile.setStatus(KYCStatus.PENDING);
        kycProfile.setSubmittedAt(LocalDateTime.now());

        String idImageKey = s3Service.uploadFile(idImage);
        String selfieImageKey = s3Service.uploadFile(selfieImage);

        return null;
    }
}
