package com.campusconnect.service;

import com.campusconnect.dto.SkillMatchResultDTO;

import java.util.List;

public interface MatchingService {
    List<SkillMatchResultDTO> matchStudentWithOpportunities(Long studentId);
    List<SkillMatchResultDTO> matchOpportunityWithStudents(Long opportunityId);
    SkillMatchResultDTO getMatchDetails(Long studentId, Long opportunityId);
}
