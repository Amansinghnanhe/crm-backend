package com.crm.crmbackend.modules.activity.repository;

import com.crm.crmbackend.modules.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Sahi naam: findByLeadId... (kyunki argument Long leadId hai)
    List<Activity> findByLeadIdOrderByActivityDateDesc(Long leadId);
}