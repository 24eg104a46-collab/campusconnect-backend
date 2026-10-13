package com.campusconnect.service.impl;

import com.campusconnect.dto.ApplicationCreateDTO;
import com.campusconnect.dto.ApplicationDTO;
import com.campusconnect.dto.ApplicationStatusUpdateDTO;
import com.campusconnect.entity.Application;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Skill;
import com.campusconnect.entity.Student;
import com.campusconnect.exception.DuplicateResourceException;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.ApplicationRepository;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.service.ApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final OpportunityRepository opportunityRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  StudentRepository studentRepository,
                                  OpportunityRepository opportunityRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public ApplicationDTO apply(ApplicationCreateDTO createDTO) {
        if (applicationRepository.existsByStudentIdAndOpportunityId(createDTO.getStudentId(), createDTO.getOpportunityId())) {
            throw new DuplicateResourceException("Student has already applied to this opportunity");
        }

        Student student = studentRepository.findByIdWithSkills(createDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + createDTO.getStudentId()));

        Opportunity opportunity = opportunityRepository.findByIdWithSkills(createDTO.getOpportunityId())
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + createDTO.getOpportunityId()));

        int matchScore = calculateMatchScore(student, opportunity);

        Application application = new Application(
                student,
                opportunity,
                createDTO.getResumeUrl(),
                createDTO.getCoverNote().trim(),
                matchScore
        );

        Application saved = applicationRepository.save(application);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAllWithDetails().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDTO getApplicationById(Long id) {
        Application application = applicationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
        return mapToDTO(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByOpportunity(Long opportunityId) {
        return applicationRepository.findByOpportunityId(opportunityId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDTO updateStatus(Long id, ApplicationStatusUpdateDTO statusDTO) {
        Application application = getApplicationEntity(id);
        application.setStatus(statusDTO.getStatus());
        return mapToDTO(applicationRepository.save(application));
    }

    @Override
    public void withdrawApplication(Long id) {
        Application application = getApplicationEntity(id);
        applicationRepository.delete(application);
    }

    @Override
    @Transactional(readOnly = true)
    public Application getApplicationEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    private int calculateMatchScore(Student student, Opportunity opportunity) {
        Set<Skill> requiredSkills = opportunity.getRequiredSkills();
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 100;
        }

        Set<String> studentSkillNames = student.getSkills().stream()
                .map(s -> s.getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        long matchedCount = requiredSkills.stream()
                .filter(s -> studentSkillNames.contains(s.getName().trim().toLowerCase()))
                .count();

        return (int) Math.round(((double) matchedCount / requiredSkills.size()) * 100.0);
    }

    private ApplicationDTO mapToDTO(Application app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());

        if (app.getStudent() != null) {
            dto.setStudentId(app.getStudent().getId());
            dto.setStudentName(app.getStudent().getFullName());
            dto.setStudentEmail(app.getStudent().getEmail());
            dto.setStudentRollNumber(app.getStudent().getRollNumber());
            dto.setStudentDepartment(app.getStudent().getDepartment());
            dto.setStudentCgpa(app.getStudent().getCgpa());
        }

        if (app.getOpportunity() != null) {
            dto.setOpportunityId(app.getOpportunity().getId());
            dto.setOpportunityTitle(app.getOpportunity().getTitle());
            dto.setOpportunityCompany(app.getOpportunity().getCompany());
            dto.setOpportunityLocation(app.getOpportunity().getLocation());
            dto.setOpportunityType(app.getOpportunity().getType() != null ? app.getOpportunity().getType().name() : null);
        }

        dto.setAppliedDate(app.getAppliedDate());
        dto.setStatus(app.getStatus());
        dto.setResumeUrl(app.getResumeUrl());
        dto.setCoverNote(app.getCoverNote());
        dto.setMatchScore(app.getMatchScore());

        return dto;
    }
}
