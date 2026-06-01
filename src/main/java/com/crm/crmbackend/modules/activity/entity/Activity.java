package com.crm.crmbackend.modules.activity.entity;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityType; // e.g., CALL, EMAIL, MEETING

    @Column(columnDefinition = "TEXT") // @Lob se behtar aur safe tarika lamba text store karne ka
    private String details;

    private LocalDateTime activityDate;

    // FIX: Quotes ko sahi kiya taaki database crash na ho
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)

    @JsonIgnoreProperties({"assignedTo", "createdAt"})
    private Lead lead;

    private String recordedByEmail;

    @PrePersist
    protected void onCreate() {
        if (this.activityDate == null) {
            this.activityDate = LocalDateTime.now();
        }
    }
}