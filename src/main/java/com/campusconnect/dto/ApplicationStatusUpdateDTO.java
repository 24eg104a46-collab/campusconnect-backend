package com.campusconnect.dto;

import com.campusconnect.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class ApplicationStatusUpdateDTO {

    @NotNull(message = "New application status is required")
    private ApplicationStatus status;

    public ApplicationStatusUpdateDTO() {}

    public ApplicationStatusUpdateDTO(ApplicationStatus status) {
        this.status = status;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
