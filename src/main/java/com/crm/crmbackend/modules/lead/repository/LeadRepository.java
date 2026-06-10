package com.crm.crmbackend.modules.lead.repository;

import com.crm.crmbackend.modules.dashboard.dto.LeadStatusCountDTO;
import com.crm.crmbackend.modules.lead.entity.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    @Query("SELECT l FROM Lead l WHERE " +
            "(:userId IS NULL OR l.assignedTo.id = :userId) AND " +
            "(:status IS NULL OR l.status = :status) AND " +
            "(:search IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(l.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Lead> findLeadsWithFilters(
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT new com.crm.crmbackend.modules.dashboard.dto.LeadStatusCountDTO(l.status, COUNT(l)) " +
            "FROM Lead l " +
            "WHERE (:userId IS NULL OR l.assignedTo.id = :userId) " +
            "GROUP BY l.status")
    List<LeadStatusCountDTO> getLeadsCountGroupedByStatus(@Param("userId") Long userId);
    long countByAssignedToId(Long userId);
}