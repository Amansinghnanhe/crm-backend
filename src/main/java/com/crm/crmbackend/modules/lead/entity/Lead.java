package com.crm.crmbackend.modules.lead.entity; // Package name fixed

import com.crm.crmbackend.modules.user.entity.User; // User ka sahi import
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Getter
@Setter
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String status;
    private String source;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedTo;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}