package com.oasis.FIFAFanWallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPassRequest(
        @NotBlank(message = "Password is required.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must be eight or more characters containing at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        String newPassword,
        @NotBlank(message = "Token is required.")
        String token
){}
