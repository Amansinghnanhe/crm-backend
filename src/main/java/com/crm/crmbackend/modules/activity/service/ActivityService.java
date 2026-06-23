package com.crm.crmbackend.modules.activity.service;

import com.crm.crmbackend.modules.activity.entity.Activity;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final LeadRepository leadRepository;

    public ActivityService(ActivityRepository activityRepository, LeadRepository leadRepository) {
        this.activityRepository = activityRepository;
        this.leadRepository = leadRepository;
    }

    @Transactional
    public Activity logActivity(Long leadId, Activity activity) {

        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));


        activity.setLead(lead);
        if (activity.getCreatedAt() == null) {
            activity.setCreatedAt(LocalDateTime.now()); // Agar entity me variable ka naam alag hai to use change kar lena (e.g., activityDate)
        }


        if ("NEW".equalsIgnoreCase(lead.getStatus())) {
            lead.setStatus("IN_PROGRESS");
            leadRepository.save(lead); // Update parent lead status in database
        }

        return activityRepository.save(activity);
    }

    public List<Activity> getActivitiesLead(Long leadId) {
        // Validation check ki lead sach me exist karta hai ya nahi
        if (!leadRepository.existsById(leadId)) {
            throw new RuntimeException("Lead not found with id: " + leadId);
        }

        // ✅ FIXED: Tumhari requirement ke hisab se dynamic query mapping call ki hai
        // Dhyan dena: Agar entity me dynamic time field 'createdAt' hai to findByLeadIdOrderByCreatedAtDesc chalega
        return activityRepository.findByLeadIdOrderByCreatedAtDesc(leadId);
    }
}