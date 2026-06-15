package com.crm.crmbackend.modules.lead.controller;

import com.crm.crmbackend.modules.lead.dto.LeadResponseDTO;
import com.crm.crmbackend.modules.lead.entity.Lead;
import com.crm.crmbackend.modules.lead.service.LeadService;
import com.crm.crmbackend.modules.user.dto.ApiResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @PathVariable("id") Long leadId,
            @RequestParam String status,
            Principal principal) {
        String loggedInUserEmail = principal.getName();
        return ResponseEntity.ok(leadService.updatedStatus(leadId, status, loggedInUserEmail));
    }

    @GetMapping
    public ResponseEntity<Page<LeadResponseDTO>> getAllLeads(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search
    ) {
        String agentEmail = principal.getName();
        Page<LeadResponseDTO> leadsPage = leadService.getAllLeadsPaged(agentEmail, status, search, page, size);
        return ResponseEntity.ok(leadsPage);
    }

    // 4. GET: Single Lead by ID
    @GetMapping("/{id}/history")
    public ResponseEntity<LeadResponseDTO> getLeadById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    // 5. DELETE: Delete Lead by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        // Standard ApiResponseDTO return kar rhe hain taaki frontend ko hmesha true/false flag mile
        return ResponseEntity.ok(new ApiResponseDTO(true, "Lead deleted successfully with id: " + id));
    }

    // =========================================================================
    //  NEW ADDED: BULK EXCEL LEAD IMPORT ENDPOINT
    // =========================================================================
    @PostMapping("/import")
    public ResponseEntity<ApiResponseDTO> uploadExcelFile(@RequestParam("file") MultipartFile file, Principal principal) {

        // 1. Check karo ki file empty toh nahi hai
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, "Kripya ek valid Excel sheet select xarein!"));
        }

        // 2. Format validation check (`.xlsx` format check)
        if (!com.crm.crmbackend.modules.lead.util.ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, "Galat format! Sirf .xlsx extension wali Excel file upload karein."));
        }

        try {
            // 3. Principal se token wale logged-in agent ki email nikal li
            String loggedInUserEmail = principal.getName();

            // 4. Service layer ko process complete karne ke liye trigger kiya
            leadService.saveExcelLeads(file, loggedInUserEmail);

            return ResponseEntity.ok(
                    new ApiResponseDTO(true, "Mubarak ho bhai! Excel sheet ki saari leads successfully import ho gayi hain.")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Lead Import Fail ho gaya: " + e.getMessage()));
        }
    }
}