package com.crm.crmbackend.modules.lead.controller;

import com.crm.crmbackend.modules.lead.dto.LeadResponseDTO;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.service.LeadService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

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
    public ResponseEntity<LeadResponseDTO> createLead(@RequestBody Lead lead, Principal principal) {
        String loggedInUserEmail = principal.getName();
        return new ResponseEntity<>(leadService.createLead(lead, loggedInUserEmail), HttpStatus.CREATED);
    }

    // 2. PATCH: Update Lead Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadResponseDTO> updateLeadStatus(
            @PathVariable Long leadId,
            @RequestParam String status,
            Principal principal) {
        String loggedInUserEmail = principal.getName();
        return ResponseEntity.ok(leadService.updatedStatus(leadId, status, loggedInUserEmail));
    }

    // 🔥 FIXED: Duplicate method ko hata kar sirf ek single working method rakha hai 5 parameters ke sath
    // 3. GET: Fetch All Leads (Paged with Filters)
    @GetMapping
    public ResponseEntity<Page<LeadResponseDTO>> getAllLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search
    ) {
        Page<LeadResponseDTO> leadsPage = leadService.getAllLeadsPaged(null, status, search, page, size);
        return ResponseEntity.ok(leadsPage);
    }

    // 4. GET: Single Lead by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeadResponseDTO> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    // 5. DELETE: Delete Lead by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return ResponseEntity.ok("Lead deleted successfully with id: " + id);
    }
}