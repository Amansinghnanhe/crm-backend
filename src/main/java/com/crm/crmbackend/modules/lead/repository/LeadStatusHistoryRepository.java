package com.crm.crmbackend.modules.lead.repository;

import com.crm.crmbackend.modules.lead.entity.LeadStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadStatusHistoryRepository extends JpaRepository<LeadStatusHistory, Long> {

    // 🚨 FIXED: 'ChangeAt' ko badal kar 'ChangedAt' kar diya hai (d jod diya hai)
    List<LeadStatusHistory> findByLeadIdOrderByChangedAtDesc(Long leadId);
}