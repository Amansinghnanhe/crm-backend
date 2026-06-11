package com.crm.crmbackend.modules.dashboard.service;

import com.crm.crmbackend.exception.ResourceNotFoundException;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import com.crm.crmbackend.modules.dashboard.dto.DashboardStatsResponseDTO;
import com.crm.crmbackend.modules.dashboard.dto.LeadStatusCountDTO;
import com.crm.crmbackend.modules.user.entity.Role;
import com.crm.crmbackend.modules.user.repository.UserRepository;
import com.crm.crmbackend.modules.user.entity.User; // 🔥 FIXED: User entity import missing thi
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {

    private final LeadRepository leadRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    // 🔥 FIXED: Constructor me UserRepository ko accept kiya taaki Spring properly inject kar sake
    public DashboardService(LeadRepository leadRepository, ActivityRepository activityRepository, UserRepository userRepository) {
        this.leadRepository = leadRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public DashboardStatsResponseDTO getDashboardStats(String agentEmail) {

        // 1. Logged in user fetch karna
        User user = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found with email: " + agentEmail));
        long totalLeads;
        long totalActivities;
        List<LeadStatusCountDTO> statusCounts;
        if (user.getRole()== Role.ROLE_AGENT) {
            totalLeads = leadRepository.countByAssignedToId(user.getId());
            totalActivities = activityRepository.countByRecordedByEmail(agentEmail);
            statusCounts = leadRepository.getLeadsCountGroupedByStatus(user.getId());
        } else {
            totalLeads = leadRepository.count();
            totalActivities = activityRepository.count();
            statusCounts = leadRepository.getLeadsCountGroupedByStatus(null);
        }


//        // 2.  FIXED: user.getId() kiya (comma hataya) aur repository name sahi kiya
//        long totalLeads = leadRepository.countByAssignedToId(user.getId());
//
//        // 3.  FIXED: Method name 'countByRecordedByEmail' kiya jo standard repository se match kare
//        long totalActivities = activityRepository.countByRecordedByEmail(agentEmail);
//
//        // 4.  FIXED: user.getId() ka syntax correct kiya aur duplicate logic hata diya
//        List<LeadStatusCountDTO> statusCounts = leadRepository.getLeadsCountGroupedByStatus(user.getId());

        // 5. Clean builder response return kiya
        return DashboardStatsResponseDTO.builder()
                .totalLeads(totalLeads)
                .totalActivities(totalActivities)
                .leadBYStatus(statusCounts)
                .build();
    }
}