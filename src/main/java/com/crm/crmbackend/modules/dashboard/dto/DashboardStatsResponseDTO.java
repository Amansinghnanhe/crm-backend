package com.crm.crmbackend.modules.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@Builder
public class DashboardStatsResponseDTO {
    private long totalLeads;
    private long totalActivities;
    private List<LeadStatusCountDTO> leadBYStatus;
}
