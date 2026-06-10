package com.crm.crmbackend.modules.lead.dto;

public class LeadResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String assignedToAgentName;

    public LeadResponseDTO(Long id, String name, String email, String phone, String status, String assignedToAgentName) {
        this.id= id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.assignedToAgentName = assignedToAgentName;

    }
    public Long getId() {
        return id;

    }
    public String getName() {


        return name;
    }
    public String getEmail() {

        return email;
    }
    public String getPhone() {

        return phone;
    }
    public String getStatus() {

        return status;
    }
    public String getAssignedToAgentName() {

        return assignedToAgentName;
    }
}
