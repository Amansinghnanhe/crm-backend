package com.crm.crmbackend.modules.lead.service;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import com.crm.crmbackend.modules.user.entity.User;
import com.crm.crmbackend.modules.activity.entity.Activity;
import com.crm.crmbackend.modules.activity.repository.ActivityRepository;
import com.crm.crmbackend.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public LeadService(LeadRepository leadRepository, UserRepository userRepository, ActivityRepository activityRepository) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
    }

    // 1. New Lead Create karna
    @Transactional
    public Lead createLead(Lead lead, String creatorEmail) {
        User user = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
        if (lead.getStatus() == null) {
            lead.setStatus("NEW");
        }
        lead.setAssignedTo(user);

        Lead savedLead = leadRepository.save(lead);

        Activity systemLog = new Activity();
        systemLog.setActivityType("SYSTEM");
        systemLog.setDetails("Lead system me create hui aur " + user.getName() + " ko assign ki gayi.");
        systemLog.setRecordedByEmail(creatorEmail);
        systemLog.setLead(savedLead); // Fixed: Duplicate line yahan se hatayi

        activityRepository.save(systemLog);
        return savedLead;
    }

    // 2. Lead Status Update karna (Sahi kiya hua method)
    @Transactional
    public Lead updatedStatus(Long leadId, String newStatus, String updatedByEmail) { // Fixed: newString ko newStatus kiya
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        String oldStatus = lead.getStatus();
        lead.setStatus(newStatus); // Ab ye bilkul sahi chalega
        Lead updatedLead = leadRepository.save(lead);

        Activity statusLog = new Activity();
        statusLog.setActivityType("STATUS_UPDATE");
        // Fixed: String concatenation ko theek kiya taaki naya status bhi print ho
        statusLog.setDetails("Lead status ko '" + oldStatus + "' se badalkar '" + newStatus + "' kiya gya.");
        statusLog.setRecordedByEmail(updatedByEmail); // Fixed: Spelling error theek ki
        statusLog.setLead(updatedLead);

        activityRepository.save(statusLog);
        return updatedLead;
    }

    // 3. Saari Leads Fetch karna
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    // ID se single lead nikalna
    public Lead getLeadById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
    }

    // Lead delete karne ke liye
    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }
}