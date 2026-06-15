package com.crm.crmbackend.modules.user.repository;

import com.crm.crmbackend.modules.user.entity.User;
import com.crm.crmbackend.modules.user.entity.Role; // 👈 Ye import pakka check karo!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    // 🌟 Ye method exact aisa hona chahiye
    List<User> findByRoleAndActive(Role role, boolean active);
}