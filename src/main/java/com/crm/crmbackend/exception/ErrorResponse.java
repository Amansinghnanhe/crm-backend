package com.crm.crmbackend.exception; // 👈 Aapke screenshot ke hisab se ye isi folder me rahega

import java.time.LocalDateTime;

public class ErrorResponse {
    private boolean success;
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;

    // Constructor
    public ErrorResponse(String message, int statusCode) {
        this.success = false;
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() { // 👈 Sahi Getter: No inputs, returns int!
        return statusCode;
    }

    public void setStatusCode(int statusCode) { // 👈 Sahi Setter: Takes input, returns void!
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}