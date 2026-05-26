package com.crm.crmbackend.modules.user.service;

import com.crm.crmbackend.config.JwtProvider;
import com.crm.crmbackend.modules.email.EmailService;
import com.crm.crmbackend.modules.user.dto.*;
import com.crm.crmbackend.modules.user.entity.User;
import com.crm.crmbackend.modules.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.emailService = emailService;

    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public String registerUser(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER");

        user.setVerified(false); // Lombok boolean primitive setter
        user.setOtp(generateRandomOtp());
        user.setOtpExpiryAt(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);
        // 🔥 REAL EMAIL BHEJNE WALI LINE (Ise add karein)
        emailService.sendEmail(user.getEmail(), "Registration OTP", "Your OTP is: " + user.getOtp());

        System.out.println("=================================================");
        System.out.println("🔥 REGISTRATION OTP FOR " + user.getEmail() + " IS: " + user.getOtp());
        System.out.println("=================================================");

        return "Registration successful! Please verify the OTP sent to your email.";
    }

    public String verifyOtp(VerifyOtpDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (user.getOtp() == null || !user.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP!");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiryAt())) {
            throw new RuntimeException("OTP has expired! Please register again or resend OTP.");
        }

        user.setVerified(true); // Lombok boolean primitive setter
        user.setOtp(null);
        user.setOtpExpiryAt(null);
        userRepository.save(user);

        return "Account verified successfully! You can now login.";
    }

    public LoginResponseDTO loginUser(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        // Primitive boolean getter call -> makkhan chalega
        if (!user.isVerified()) {
            throw new RuntimeException("Your account is not verified! Please verify your OTP first.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole());
        return new LoginResponseDTO(true, "Login successful", token, user.getRole());
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email!"));

        user.setOtp(generateRandomOtp());
        user.setOtpExpiryAt(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        System.out.println("=================================================");
        System.out.println("🔑 FORGOT PASSWORD OTP FOR " + email + " IS: " + user.getOtp());
        System.out.println("=================================================");

        return "Password reset OTP has been sent to your email.";
    }

    public String resetPassword(ResetPasswordDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (user.getOtp() == null || !user.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP!");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiryAt())) {
            throw new RuntimeException("OTP has expired!");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiryAt(null);
        userRepository.save(user);

        return "Password has been reset successfully!";
    }
}