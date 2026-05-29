package com.crm.crmbackend.modules.activity.service;

import com.crm.crmbackend.modules.activity.entity.Activity;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final LeadRepository leadRepository;


    public ActivityService(ActivityRepository activityRepository, LeadRepository leadRepository) {
        this.activityRepository = activityRepository;
        this.leadRepository = leadRepository;
    }

    public Activity logActivity(Long leadId, Activity activity) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        activity.setLead(lead);
        return activityRepository.save(activity);
    }
    public List<Activity> getActivitiesLead(Long leadId) {
        if (!leadRepository.existsById(leadId)) {
            throw new RuntimeException("Lead not found");
        }
        // YAHAN CHANGE KARNA HAI: findByLeadId... likhna hai
        return activityRepository.findByLeadIdOrderByActivityDateDesc(leadId);
    }
}
