package com.crm.crmbackend.modules.lead.service;

import com.crm.crmbackend.exception.ResourceNotFoundException;
import com.crm.crmbackend.modules.email.EmailService;
import com.crm.crmbackend.modules.lead.dto.LeadResponseDTO;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import com.crm.crmbackend.modules.user.entity.User;
import com.crm.crmbackend.modules.activity.entity.Activity;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final EmailService emailService;

    public LeadService(LeadRepository leadRepository, UserRepository userRepository, ActivityRepository activityRepository, EmailService emailService) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.emailService = emailService;
    }

    // Helper Method: Lead Entity ko LeadResponseDTO me convert karne ke liye
    private LeadResponseDTO convertToDTO(Lead lead) {
        String agentName = (lead.getAssignedTo() != null) ? lead.getAssignedTo().getName() : "Unassigned";
        return new LeadResponseDTO(
                lead.getId(),
                lead.getName(),
                lead.getEmail(),
                lead.getPhone(),
                lead.getStatus(),
                agentName
        );
    }

    // 1. New Lead Create karna (Method type badal kar LeadResponseDTO kiya)
    @Transactional
    public LeadResponseDTO createLead(Lead lead, String creatorEmail) {
        User user = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found with email: " + creatorEmail));
        if (lead.getStatus() == null) {
            lead.setStatus("NEW");
        }
        lead.setAssignedTo(user);

        Lead savedLead = leadRepository.save(lead);

        Activity systemLog = new Activity();
        systemLog.setActivityType("SYSTEM");
        systemLog.setDetails("Lead system me create hui aur " + user.getName() + " ko assign ki gayi.");
        systemLog.setRecordedByEmail(creatorEmail);
        systemLog.setLead(savedLead);

        activityRepository.save(systemLog);

        String subject = " NEW Lead Assigned: " + savedLead.getName();
        String body = "Hello " + user.getName() + ",\n\nA new lead named '" + savedLead.getName()+
                "' has been successfully assigned to you .\n\nPlease check your CRM dashboard. \n\nRegards,\nTeam CRM";
        emailService.sendEmail(user.getEmail(), subject,body);

        return convertToDTO(savedLead);
    }

    // 2. Lead Status Update karna (Method type badal kar LeadResponseDTO kiya)
    @Transactional
    public LeadResponseDTO updatedStatus(Long leadId, String newStatus, String updatedByEmail) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + leadId));

        String oldStatus = lead.getStatus();
        lead.setStatus(newStatus);
        Lead updatedLead = leadRepository.save(lead);

        Activity statusLog = new Activity();
        statusLog.setActivityType("STATUS_UPDATE");
        statusLog.setDetails("Lead status ko '" + oldStatus + "' se badalkar '" + newStatus + "' kiya gya.");
        statusLog.setRecordedByEmail(updatedByEmail);
        statusLog.setLead(updatedLead);

        activityRepository.save(statusLog);

        if(updatedLead.getAssignedTo() != null){
            String subject = " Lead Status Update: " + updatedLead.getName();
            String body = "The status of lead '" + updatedLead.getName() +"' has been changed from '"
                    +oldStatus + "'to '"+ newStatus + "' by " + updatedByEmail + ".";
            emailService.sendEmail(updatedLead.getAssignedTo().getEmail(), subject, body);
        }

        return convertToDTO(updatedLead);
    }
    public Page<LeadResponseDTO> getAllLeadsPaged(String status, String search, int page, int size){
        Pageable pageable = PageRequest.of(page, size);


        String statusFilter = (status != null && !status.isEmpty()) ? status : null;
        String searchFilter = (search != null && !search.isEmpty()) ? search : null;

        Page<Lead>  leadPage = leadRepository.findLeadsWithFilters(statusFilter, searchFilter, pageable);
        return leadPage.map(this::convertToDTO);
    }

    // 3. Saari Leads Fetch karna (Syntax structure aur method type fix kiya)
    public List<LeadResponseDTO> getAllLeads() {
        return leadRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()); // 👈 Fixed syntax and spelling here
    }

    // 4. ID se single lead nikalna (Method type badal kar LeadResponseDTO kiya)
    public LeadResponseDTO getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        return convertToDTO(lead);
    }

    // 5. Lead delete karne ke liye
    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }
}