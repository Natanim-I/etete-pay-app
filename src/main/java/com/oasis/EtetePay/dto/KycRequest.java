package com.oasis.EtetePay.dto;

import com.oasis.EtetePay.annotation.Adult;
import com.oasis.EtetePay.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record KycRequest(
        @NotBlank(message = "Fayda number is required.")
        @Size(min = 12, max = 12, message = "Fayda number must be exactly 12 characters.")
        String faydaNumber,
        @NotBlank(message = "First name is required.")
        @Size(max = 50, message = "First name must not exceed 50 characters.")
        String firstName,
        @Size(max = 50, message = "Middle name must not exceed 50 characters.")
        String middleName,
        @NotBlank(message = "Last name is required.")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,
        @NotNull(message = "Date of birth is required.")
        @Past(message = "Date of birth must be in the past.")
        @Adult(message = "You must be at least 18 years old.")
        LocalDate dateOfBirth,
        @NotBlank(message = "Phone number is required.")
        @ValidPhoneNumber(message = "Invalid phone number format.")
        String phoneNumber
) {}
