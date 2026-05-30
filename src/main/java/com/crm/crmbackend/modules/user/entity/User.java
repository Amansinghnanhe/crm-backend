package com.crm.crmbackend.modules.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // MySQL mein automatic 'users' naam ki table banegi
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) // Same email se do accounts nahi ban sakte
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // Jaise: ADMIN, USER

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry_at")
    private LocalDateTime otpExpiryAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Record insert hote hi current time automatic set hoga
    }
}