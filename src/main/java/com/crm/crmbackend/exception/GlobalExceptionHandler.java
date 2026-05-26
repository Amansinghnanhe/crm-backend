package com.crm.crmbackend.exception;

import com.crm.crmbackend.modules.user.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Isse validation fail hone par custom message milega frontend ko (@NotBlank, @Size etc ke liye)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(new ApiResponseDTO(false, errorMessage));
    }

    // Runtime errors ko handle karne ke liye (Jaise: "Invalid OTP!", "User not found!")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO> handleRuntimeExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ApiResponseDTO(false, ex.getMessage()));
    }
}