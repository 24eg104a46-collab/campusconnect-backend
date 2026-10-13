package com.campusconnect.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {
    private long totalStudents;
    private long totalOpportunities;
    private long totalApplications;
    private long totalSkills;
    private double averageCgpa;
    private Map<String, Long> applicationsByStatus = new HashMap<>();
    private List<SkillDTO> topInDemandSkills;
    private Map<String, Long> opportunitiesByType = new HashMap<>();

    public DashboardStatsDTO() {}

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalOpportunities() {
        return totalOpportunities;
    }

    public void setTotalOpportunities(long totalOpportunities) {
        this.totalOpportunities = totalOpportunities;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(long totalSkills) {
        this.totalSkills = totalSkills;
    }

    public double getAverageCgpa() {
        return averageCgpa;
    }

    public void setAverageCgpa(double averageCgpa) {
        this.averageCgpa = averageCgpa;
    }

    public Map<String, Long> getApplicationsByStatus() {
        return applicationsByStatus;
    }

    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) {
        this.applicationsByStatus = applicationsByStatus;
    }

    public List<SkillDTO> getTopInDemandSkills() {
        return topInDemandSkills;
    }

    public void setTopInDemandSkills(List<SkillDTO> topInDemandSkills) {
        this.topInDemandSkills = topInDemandSkills;
    }

    public Map<String, Long> getOpportunitiesByType() {
        return opportunitiesByType;
    }

    public void setOpportunitiesByType(Map<String, Long> opportunitiesByType) {
        this.opportunitiesByType = opportunitiesByType;
    }
}
