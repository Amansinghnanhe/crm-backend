package com.crm.crmbackend.modules.user.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message= "OTP cannot be empty")
    private String otp;
    @NotBlank(message = "New password cannot be happy")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
}
