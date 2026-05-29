package com.crm.crmbackend.modules.lead.controller;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.service.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/leads")
@CrossOrigin(origins = "http://localhost:5173") // React Frontend port configuration
public class LeadController {

    // Best Practice: final variable aur Constructor Injection
    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    // 1. POST: Create a New Lead
    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        // HttpStatus.CREATED (201) return karna POST requests ke liye standard hai
        return new ResponseEntity<>(leadService.createLead(lead), HttpStatus.CREATED);
    }

    // 2. GET: Fetch All Leads
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    // 🌟 ADDED 1: Get Single Lead by ID (React mein details page ke liye kaam aayega)
    // URL: GET http://localhost:8080/leads/1
    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    // 🌟 ADDED 2: Delete Lead by ID
    // URL: DELETE http://localhost:8080/leads/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok("Lead deleted successfully with id: " + id);
    }
}