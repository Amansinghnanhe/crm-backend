package com.crm.crmbackend.modules.user.repository;

import com.crm.crmbackend.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Iski madad se hum check karenge ki email pehle se exists karta hai ya nahi
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    // Login ke waqt email se user ka password dhoodhne ke liye
    Optional<User> findByEmail(String email);
}