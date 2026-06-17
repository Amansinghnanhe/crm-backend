package com.crm.crmbackend.modules.lead.entity; // Package name fixed

import com.crm.crmbackend.modules.user.entity.User; // User ka sahi import
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String status;
    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")

    @JsonIgnoreProperties({"Password", "otp", "otpExpiryAt", "createdAt", "Verified", "roles", "handler", "hibernateLazyInitializer"})

    private User assignedTo;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();


        if (this.status == null) {
            this.status = "NEW";
        }
    }
}