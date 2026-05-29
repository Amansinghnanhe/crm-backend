package com.crm.crmbackend.modules.activity.entity;

import com.crm.crmbackend.modules.lead.entity.Lead;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Data

public class Activity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;


    private String activityType;


    @Lob
    private String details;

    private LocalDateTime activityDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id, nullable = false")
    private Lead lead;

    private String recordedByEmail;
    @PrePersist
    protected void onCreate() {
        if(this.activityDate == null) {
            this.activityDate = LocalDateTime.now();
        }
    }
}
