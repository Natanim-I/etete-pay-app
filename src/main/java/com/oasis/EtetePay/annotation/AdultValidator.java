package com.oasis.EtetePay.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    private int minimumAge;

    @Override
    public void initialize(Adult constraintAnnotation) {
        this.minimumAge = constraintAnnotation.age();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true; // Let @NotNull handle null values
        }

        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= minimumAge;
    }
}