package com.campusconnect.dto;

import com.campusconnect.entity.SkillCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SkillDTO {
    private Long id;

    @NotBlank(message = "Skill name cannot be empty")
    @Size(min = 2, max = 50, message = "Skill name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Category is required")
    private SkillCategory category;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Long studentCount;
    private Long opportunityCount;

    public SkillDTO() {}

    public SkillDTO(Long id, String name, SkillCategory category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Long studentCount) {
        this.studentCount = studentCount;
    }

    public Long getOpportunityCount() {
        return opportunityCount;
    }

    public void setOpportunityCount(Long opportunityCount) {
        this.opportunityCount = opportunityCount;
    }
}
