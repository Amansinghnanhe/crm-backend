package com.crm.crmbackend.modules.lead.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lead_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    private String oldStatus;
    private String newStatus;
    private String changeByEmail;
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreation() {
        if (this.changedAt == null) {
            this.changedAt = LocalDateTime.now();
        }
    }
}
