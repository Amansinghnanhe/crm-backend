package com.crm.crmbackend.modules.lead.service;

import com.crm.crmbackend.exception.ResourceNotFoundException;
import com.crm.crmbackend.modules.email.EmailService;
import com.crm.crmbackend.modules.lead.dto.LeadResponseDTO;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.entity.LeadStatusHistory;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import com.crm.crmbackend.modules.lead.repository.LeadStatusHistoryRepository;
import com.crm.crmbackend.modules.lead.util.ExcelHelper;
import com.crm.crmbackend.modules.user.entity.Role;
import com.crm.crmbackend.modules.user.entity.User;
import com.crm.crmbackend.modules.activity.entity.Activity;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final EmailService emailService;
    private final LeadStatusHistoryRepository leadStatusHistoryRepository;

    public LeadService(LeadRepository leadRepository, UserRepository userRepository, ActivityRepository activityRepository, EmailService emailService, LeadStatusHistoryRepository leadStatusHistoryRepository) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.emailService = emailService;
        this.leadStatusHistoryRepository = leadStatusHistoryRepository;
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

        LeadStatusHistory history = LeadStatusHistory.builder()
                .lead(updatedLead)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changeByEmail(updatedByEmail)
                .changedAt(LocalDateTime.now())
                .build();
        leadStatusHistoryRepository.save(history);

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
    public Page<LeadResponseDTO> getAllLeadsPaged(  String agentEmail, String status, String search, int page, int size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);


        String statusFilter = (status != null && !status.isEmpty()) ? status : null;
        String searchFilter = (search != null && !search.isEmpty()) ? search : null;

        User user = userRepository.findByEmail(agentEmail)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email: "+ agentEmail));

        Long userIdFilter = null;

//        if(user.getRole() == Role.ROLE_AGENT){
//            userIdFilter = user.getId();
//        }
        if (user.getRole() != null && "ROLE_AGENT".equalsIgnoreCase(user.getRole().toString())) {
            userIdFilter = user.getId();
        }

        Page<Lead>  leadPage = leadRepository.findLeadsWithFilters( userIdFilter, statusFilter, searchFilter, pageable);
        return leadPage.map(this::convertToDTO);
    }

    // 3. Saari Leads Fetch karna (Syntax structure aur method type fix kiya)
    public List<LeadResponseDTO> getAllLeads() {
        return leadRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()); //  Fixed syntax and spelling here
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


    @Transactional
    public void saveExcelLeads(MultipartFile file, String loggedInUserEmail) {
        try {
            // 1. Excel to Entity Conversion Utility Class ko trigger kiya
            List<Lead> leads = ExcelHelper.excelToLeads(file.getInputStream());

            // 2. Token email se current authentic agent context fetch kiya
            User currentAgent = userRepository.findByEmail(loggedInUserEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found with email: " + loggedInUserEmail));

            // 3. Sari dynamic leads ko is unique Agent ID par lock kiya
            for (Lead lead : leads) {
                lead.setAssignedTo(currentAgent);
                if (lead.getStatus() == null || lead.getStatus().isEmpty()) {
                    lead.setStatus("NEW");
                }
            }

            // 4. Batch Performance Injection into MySQL Table
            List<Lead> savedLeads = leadRepository.saveAll(leads);

            // 5. System Task Audit trail save kiya Activity Repository me
            Activity bulkImportLog = new Activity();
            bulkImportLog.setActivityType("SYSTEM_BULK_IMPORT");
            bulkImportLog.setDetails("Excel sheet se kul " + savedLeads.size() + " leads system me bulk-import ki gayi hain.");
            bulkImportLog.setRecordedByEmail(loggedInUserEmail);
            activityRepository.save(bulkImportLog);

            // 6. Final Summary Notification Email Trigger
            String subject = "📊 CRM Bulk Lead Import Summary Notification";
            String body = "Hello " + currentAgent.getName() + ",\n\nYour Excel sheet processing is complete.\n" +
                    "Total " + savedLeads.size() + " leads have been successfully mapped and uploaded into your account.\n\n" +
                    "Please check your LeadFlux pipeline dashboard.\n\nRegards,\nTeam LeadFlux Backend Engine";
            emailService.sendEmail(currentAgent.getEmail(), subject, body);

        } catch (IOException e) {
            throw new RuntimeException("Excel data save karne me takleef aayi hai: " + e.getMessage());
        }
    }
}