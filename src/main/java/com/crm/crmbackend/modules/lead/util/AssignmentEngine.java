package com.crm.crmbackend.modules.lead.util;

import com.crm.crmbackend.modules.user.entity.User;
import com.crm.crmbackend.modules.user.entity.Role; // Enum import check kar lena
import com.crm.crmbackend.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AssignmentEngine {

    @Autowired
    private UserRepository userRepository;

    private int lastAssignedIndex = -1;

    public synchronized User getNextAgent() {
        // 🔥 FIXED: 'Role.AGENT' ki jagah tumhare enum ke hisab se 'Role.ROLE_AGENT' kar diya hai
        List<User> activeAgents = userRepository.findByRoleAndActive(Role.ROLE_AGENT, true);

        if (activeAgents.isEmpty()) {
            return null;
        }

        // Round-robin logic
        lastAssignedIndex = (lastAssignedIndex + 1) % activeAgents.size();
        return activeAgents.get(lastAssignedIndex);
    }
}