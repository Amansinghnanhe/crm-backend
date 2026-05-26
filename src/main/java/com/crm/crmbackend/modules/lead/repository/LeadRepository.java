package com.crm.crmbackend.modules.lead.repository;

import com.crm.crmbackend.modules.lead.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
}