package com.oasis.EtetePay.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // Ethiopia
    private static final String ETHIOPIA =
            "^(\\+251|251|0)?9\\d{8}$";

    // United States
    private static final String USA =
            "^(\\+1|1)?[2-9]\\d{2}[2-9]\\d{6}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true; // @NotBlank handles this
        }

        // Remove spaces, dashes, parentheses
        String normalized = phoneNumber.replaceAll("[\\s()-]", "");

        return normalized.matches(ETHIOPIA)
                || normalized.matches(USA);
    }
}