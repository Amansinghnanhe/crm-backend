package com.crm.crmbackend.modules.lead.service;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeadService {

    // Best Practice: Variable ko final banayein
    private final LeadRepository leadRepository;

    // Best Practice: Constructor Injection use karein (@Autowired ki zaroorat nahi hai yahan)
    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    // 1. New Lead Create karna
    public Lead createLead(Lead lead) {
        // Aap yahan default status bhi set kar sakte hain agar frontend se na aaye
        if (lead.getStatus() == null) {
            lead.setStatus("NEW");
        }
        return leadRepository.save(lead);
    }

    // 2. Saari Leads Fetch karna
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    // 🌟 BONUS METHOD 1: ID se single lead nikalna (Aage kaam aayega)
    public Lead getLeadById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
    }

    // 🌟 BONUS METHOD 2: Lead delete karne ke liye
    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException("Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
    }
}