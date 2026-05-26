package com.crm.crmbackend.modules.lead.service;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeadService {
    @Autowired
    private LeadRepository leadRepository;

    public Lead createLead(Lead lead) {
        return leadRepository.save(lead);
    }

    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }
}