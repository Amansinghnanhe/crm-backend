package com.crm.crmbackend.modules.lead.dto;

import lombok.*;


public class LeadResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String assignedToAgentName;

    public LeadResponseDTO() {

    }

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

    public void seid(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.email = email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssignedToAgentName(String assignedToAgentName) {
        this.assignedToAgentName = assignedToAgentName;
    }

}
