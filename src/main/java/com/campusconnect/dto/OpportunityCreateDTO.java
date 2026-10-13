package com.campusconnect.dto;

import com.campusconnect.entity.OpportunityType;
import com.campusconnect.entity.WorkMode;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class OpportunityCreateDTO {

    @NotBlank(message = "Opportunity title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Company or host organization name is required")
    private String company;

    @NotNull(message = "Opportunity type is required")
    private OpportunityType type;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Work mode is required (REMOTE, HYBRID, ON_SITE)")
    private WorkMode workMode;

    private String stipendOrSalary;

    @DecimalMin(value = "0.0", message = "Minimum CGPA cannot be negative")
    @DecimalMax(value = "10.0", message = "Minimum CGPA cannot exceed 10.0")
    private Double minCgpa;

    @FutureOrPresent(message = "Application deadline must be today or in the future")
    private LocalDate deadline;

    @NotBlank(message = "Detailed job/opportunity description is required")
    @Size(max = 3000, message = "Description cannot exceed 3000 characters")
    private String description;

    private Boolean isActive = true;

    private Set<Long> requiredSkillIds = new HashSet<>();

    public OpportunityCreateDTO() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public OpportunityType getType() {
        return type;
    }

    public void setType(OpportunityType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public String getStipendOrSalary() {
        return stipendOrSalary;
    }

    public void setStipendOrSalary(String stipendOrSalary) {
        this.stipendOrSalary = stipendOrSalary;
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Set<Long> getRequiredSkillIds() {
        return requiredSkillIds;
    }

    public void setRequiredSkillIds(Set<Long> requiredSkillIds) {
        this.requiredSkillIds = requiredSkillIds;
    }
}
