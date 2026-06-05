package com.crm.crmbackend.modules.dashboard.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LeadStatusCountDTO {
    private String status;
    private long count;
}
