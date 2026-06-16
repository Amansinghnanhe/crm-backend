package com.crm.crmbackend.modules.activity.controller;

import com.crm.crmbackend.modules.activity.entity.Activity;
import com.crm.crmbackend.modules.activity.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
// 👇 Fix: Humne URL variable ka naam '{leadId}' kar diya hai taaki method se match kare
@RequestMapping("/leads/{leadId}/activities")
@CrossOrigin(origins = "http://localhost:5173")
//@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<Activity> logActivity(@PathVariable Long leadId, @RequestBody Activity activity, java.security.Principal principal) {

//        // 🔥 CONSOLE LOGS: Postman se data aate hi terminal par dikhega
//        System.out.println("\n==============================================");
//        System.out.println("🚀 [POSTMAN SE REQUEST AAYI] - Log Activity");
//        System.out.println("📍 Path Variable (Lead ID): " + leadId);
//        System.out.println("📝 Activity Type: " + activity.getActivityType());
//        System.out.println("💬 Details: " + activity.getDetails());
//        System.out.println("📧 Recorded By Email: " + activity.getRecordedByEmail());
//        System.out.println("==============================================\n");

        // Status code ko bhi 201 Created kar dete hain jo POST ke liye best practice hai
        activity.setRecordedByEmail(principal.getName());
        return new ResponseEntity<>(activityService.logActivity(leadId, activity), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getLeadHistory(@PathVariable Long leadId, @RequestBody Activity activity, Principal principal) {

//        // 🔥 CONSOLE LOGS: History fetch karte waqt dikhega
//        System.out.println("\n==============================================");
//        System.out.println("🔍 [GET REQUEST AAYI] - Fetching History");
//        System.out.println("📍 Requesting history for Lead ID: " + leadId);
//        System.out.println("==============================================\n");

        return ResponseEntity.ok(activityService.getActivitiesLead(leadId));
    }
}