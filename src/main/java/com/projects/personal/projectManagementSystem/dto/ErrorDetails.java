package com.projects.personal.projectManagementSystem.dto;

import java.time.LocalDateTime;

public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String errorCode;

    public ErrorDetails(LocalDateTime timestamp, String message, String errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
