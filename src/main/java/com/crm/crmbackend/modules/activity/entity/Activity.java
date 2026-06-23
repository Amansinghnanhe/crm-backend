package com.crm.crmbackend.modules.activity.entity;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityType; // e.g., CALL, EMAIL, MEETING

    @Column(columnDefinition = "TEXT") // @Lob se behtar aur safe tarika lamba text store karne ka
    private String details;

    private String recordedByEmail;

//    private LocalDateTime activityDate;
    private LocalDateTime createdAt;

    // FIX: Quotes ko sahi kiya taaki database crash na ho
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)

    @JsonIgnoreProperties({"assignedTo", "createdAt", "handler", "hibernateLazyInitializer"})
    private Lead lead;


    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}