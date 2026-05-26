package com.crm.crmbackend.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Getter aur Setter (ya @Data agar lombok use kar rahe ho)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}