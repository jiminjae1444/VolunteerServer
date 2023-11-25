package com.example.Volunteer.controller;

public class ApplicationResponse {
    private boolean success;
    private Long applicationId;
    private String message;
 public ApplicationResponse(boolean success, String message , Long applicationId) {
     this.success=success;
     this.message=message;
     this.applicationId=applicationId;
 }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
