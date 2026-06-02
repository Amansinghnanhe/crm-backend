package com.crm.crmbackend.modules.lead.controller;

import com.crm.crmbackend.modules.lead.dto.LeadResponseDTO; // 👈 DTO import hona zaroori hai
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

    // 1. POST: Create a New Lead (Badalkar LeadResponseDTO kiya)
    @PostMapping
    public ResponseEntity<LeadResponseDTO> createLead(@RequestBody Lead lead, Principal principal) {
        String loggedInUserEmail = principal.getName();
        return new ResponseEntity<>(leadService.createLead(lead, loggedInUserEmail), HttpStatus.CREATED);
    }

    // 2. PATCH: Update Lead Status (Badalkar LeadResponseDTO kiya)
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadResponseDTO> updateLeadStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Principal principal) {
        String loggedInUserEmail = principal.getName();
        return ResponseEntity.ok(leadService.updatedStatus(id, status, loggedInUserEmail));
    }

    // 3. GET: Fetch All Leads (Badalkar List<LeadResponseDTO> kiya)
    @GetMapping
    public ResponseEntity<List<LeadResponseDTO>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    // 4. GET: Single Lead by ID (Badalkar LeadResponseDTO kiya)
    @GetMapping("/{id}")
    public ResponseEntity<LeadResponseDTO> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    // 5. DELETE: Delete Lead by ID (Yeh String hi return karega)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok("Lead deleted successfully with id: " + id);
    }
}