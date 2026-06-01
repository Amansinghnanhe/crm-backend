package com.crm.crmbackend.modules.lead.controller;

import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.service.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/leads")
@CrossOrigin(origins = "http://localhost:5173")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    // 1. POST: Create a New Lead
    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead, Principal principal) {
        String loggedInUserEmail = principal.getName();
        return new ResponseEntity<>(leadService.createLead(lead, loggedInUserEmail), HttpStatus.CREATED);
    }

    // 2. PATCH: Update Lead Status (Fixed Method Name Here)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Lead> updateLeadStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Principal principal) {
        String loggedInUserEmail = principal.getName();
        // Fixed: LeadService ke 'updatedStatus' method ko sahi naam se call kiya
        return ResponseEntity.ok(leadService.updatedStatus(id, status, loggedInUserEmail));
    }

    // 3. GET: Fetch All Leads
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    // 4. GET: Single Lead by ID
    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    // 5. DELETE: Delete Lead by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok("Lead deleted successfully with id: " + id);
    }
}