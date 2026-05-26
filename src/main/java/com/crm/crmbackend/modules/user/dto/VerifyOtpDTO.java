package com.crm.crmbackend.modules.user.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VerifyOtpDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message= "Invalid email format")
    private String email;
    @NotBlank(message = "Otp cannot be empty")
    private String otp;
}
