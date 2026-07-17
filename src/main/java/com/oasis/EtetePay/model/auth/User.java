package com.oasis.EtetePay.model.auth;

import com.oasis.EtetePay.enums.Country;
import com.oasis.EtetePay.model.KYCProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
@Component
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(unique = true)
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Country country;
    private boolean enabled = false;
    private boolean locked = false;
    private String verificationToken;
    private LocalDateTime tokenExpiry;
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private KYCProfile kycProfile;
}
