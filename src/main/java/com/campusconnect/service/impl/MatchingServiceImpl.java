package com.campusconnect.service.impl;

import com.campusconnect.dto.SkillMatchResultDTO;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Skill;
import com.campusconnect.entity.Student;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.service.MatchingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MatchingServiceImpl implements MatchingService {

    private final StudentRepository studentRepository;
    private final OpportunityRepository opportunityRepository;

    public MatchingServiceImpl(StudentRepository studentRepository, OpportunityRepository opportunityRepository) {
        this.studentRepository = studentRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public List<SkillMatchResultDTO> matchStudentWithOpportunities(Long studentId) {
        Student student = studentRepository.findByIdWithSkills(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<Opportunity> opportunities = opportunityRepository.findAllActiveWithSkills();

        return opportunities.stream()
                .map(opp -> computeMatch(student, opp))
                .sorted(Comparator.comparingInt(SkillMatchResultDTO::getMatchPercentage).reversed()
                        .thenComparing(SkillMatchResultDTO::isEligibleByCgpa).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillMatchResultDTO> matchOpportunityWithStudents(Long opportunityId) {
        Opportunity opportunity = opportunityRepository.findByIdWithSkills(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + opportunityId));

        List<Student> students = studentRepository.findAllWithSkills();

        return students.stream()
                .map(student -> computeMatch(student, opportunity))
                .sorted(Comparator.comparingInt(SkillMatchResultDTO::getMatchPercentage).reversed()
                        .thenComparing(SkillMatchResultDTO::getStudentCgpa).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public SkillMatchResultDTO getMatchDetails(Long studentId, Long opportunityId) {
        Student student = studentRepository.findByIdWithSkills(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        Opportunity opportunity = opportunityRepository.findByIdWithSkills(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + opportunityId));

        return computeMatch(student, opportunity);
    }

    private SkillMatchResultDTO computeMatch(Student student, Opportunity opportunity) {
        SkillMatchResultDTO result = new SkillMatchResultDTO();
        result.setStudentId(student.getId());
        result.setStudentName(student.getFullName());
        result.setStudentCgpa(student.getCgpa());

        result.setOpportunityId(opportunity.getId());
        result.setOpportunityTitle(opportunity.getTitle());
        result.setOpportunityCompany(opportunity.getCompany());
        result.setMinCgpa(opportunity.getMinCgpa());

        boolean eligible = opportunity.getMinCgpa() == null || student.getCgpa() >= opportunity.getMinCgpa();
        result.setEligibleByCgpa(eligible);

        Set<String> studentSkillNames = student.getSkills().stream()
                .map(s -> s.getName().trim())
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> requiredSkillNames = opportunity.getRequiredSkills().stream()
                .map(s -> s.getName().trim())
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> matched = new HashSet<>();
        Set<String> missing = new HashSet<>();

        for (String req : requiredSkillNames) {
            boolean found = studentSkillNames.stream()
                    .anyMatch(s -> s.equalsIgnoreCase(req));
            if (found) {
                matched.add(req);
            } else {
                missing.add(req);
            }
        }

        result.setMatchedSkills(matched);
        result.setMissingSkills(missing);

        int matchPercent;
        if (requiredSkillNames.isEmpty()) {
            matchPercent = 100;
        } else {
            matchPercent = (int) Math.round(((double) matched.size() / requiredSkillNames.size()) * 100.0);
        }
        result.setMatchPercentage(matchPercent);

        if (matchPercent >= 85 && eligible) {
            result.setRecommendationBadge("⭐ Top Match");
        } else if (matchPercent >= 60 && eligible) {
            result.setRecommendationBadge("✨ High Match");
        } else if (matchPercent >= 40) {
            result.setRecommendationBadge("⚡ Moderate Match");
        } else {
            result.setRecommendationBadge("📚 Upskill Recommended");
        }

        return result;
    }
}
