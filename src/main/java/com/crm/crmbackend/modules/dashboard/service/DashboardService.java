package com.crm.crmbackend.modules.dashboard.service;

import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import com.crm.crmbackend.modules.dashboard.dto.DashboardStatsResponseDTO;
import com.crm.crmbackend.modules.dashboard.dto.LeadStatusCountDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {

    private final LeadRepository leadRepository;
    private final ActivityRepository activityRepository;

    public DashboardService(LeadRepository leadRepository, ActivityRepository activityRepository) {
        this.leadRepository = leadRepository;
        this.activityRepository = activityRepository;
    }

    public DashboardStatsResponseDTO getDashboardStats() {
        long totalLeads = leadRepository.count();

        // 🚀 FIXED: Yahan spelling 'totalActivities' kar di hai taaki niche builder se match ho jaye
        long totalActivities = activityRepository.count();

        List<LeadStatusCountDTO> statusCounts = leadRepository.getLeadsCountGroupedByStatus(null);

        return DashboardStatsResponseDTO.builder()
                .totalLeads(totalLeads)
                .totalActivities(totalActivities) // 🔥 Ab ye upar wale variable se perfectly match karega
                .leadBYStatus(statusCounts)        // Tumhare DTO ke capital BY se sync hai
                .build();
    }
}