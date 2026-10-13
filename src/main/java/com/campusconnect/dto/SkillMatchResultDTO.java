package com.campusconnect.dto;

import java.util.HashSet;
import java.util.Set;

public class SkillMatchResultDTO {
    private Long studentId;
    private String studentName;
    private Double studentCgpa;
    private Long opportunityId;
    private String opportunityTitle;
    private String opportunityCompany;
    private Double minCgpa;
    private int matchPercentage;
    private boolean eligibleByCgpa;
    private String recommendationBadge;
    private Set<String> matchedSkills = new HashSet<>();
    private Set<String> missingSkills = new HashSet<>();

    public SkillMatchResultDTO() {}

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getStudentCgpa() {
        return studentCgpa;
    }

    public void setStudentCgpa(Double studentCgpa) {
        this.studentCgpa = studentCgpa;
    }

    public Long getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Long opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityTitle() {
        return opportunityTitle;
    }

    public void setOpportunityTitle(String opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
    }

    public String getOpportunityCompany() {
        return opportunityCompany;
    }

    public void setOpportunityCompany(String opportunityCompany) {
        this.opportunityCompany = opportunityCompany;
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public boolean isEligibleByCgpa() {
        return eligibleByCgpa;
    }

    public void setEligibleByCgpa(boolean eligibleByCgpa) {
        this.eligibleByCgpa = eligibleByCgpa;
    }

    public String getRecommendationBadge() {
        return recommendationBadge;
    }

    public void setRecommendationBadge(String recommendationBadge) {
        this.recommendationBadge = recommendationBadge;
    }

    public Set<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(Set<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public Set<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(Set<String> missingSkills) {
        this.missingSkills = missingSkills;
    }
}
