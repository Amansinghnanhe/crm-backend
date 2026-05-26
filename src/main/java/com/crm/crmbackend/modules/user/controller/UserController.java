package com.crm.crmbackend.modules.user.controller;

import com.crm.crmbackend.modules.user.dto.*;
import com.crm.crmbackend.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO dto
    ) {
        String msg = userService.registerUser(dto);
        return ResponseEntity.ok(new ApiResponseDTO(true, msg));
    }

    // VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDTO> verifyOtp(
            @Valid @RequestBody VerifyOtpDTO dto
    ) {
        String msg = userService.verifyOtp(dto);
        return ResponseEntity.ok(new ApiResponseDTO(true, msg));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto
    ) {
        LoginResponseDTO response = userService.loginUser(dto);
        return ResponseEntity.ok(response);
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto // 👈 RequestParam hata kar ye lagaya
    ) {
        String msg = userService.forgotPassword(dto.getEmail()); // 👈 email ki jagah dto.getEmail() kiya
        return ResponseEntity.ok(new ApiResponseDTO(true, msg));
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto
    ) {
        String msg = userService.resetPassword(dto);
        return ResponseEntity.ok(new ApiResponseDTO(true, msg));
    }
}