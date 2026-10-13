package com.campusconnect.service.impl;

import com.campusconnect.dto.OpportunityCreateDTO;
import com.campusconnect.dto.OpportunityDTO;
import com.campusconnect.dto.SkillDTO;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Skill;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.ApplicationRepository;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.SkillRepository;
import com.campusconnect.service.OpportunityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final SkillRepository skillRepository;
    private final ApplicationRepository applicationRepository;

    public OpportunityServiceImpl(OpportunityRepository opportunityRepository,
                                  SkillRepository skillRepository,
                                  ApplicationRepository applicationRepository) {
        this.opportunityRepository = opportunityRepository;
        this.skillRepository = skillRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getAllOpportunities() {
        return opportunityRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> getActiveOpportunities() {
        return opportunityRepository.findAllActiveWithSkills().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OpportunityDTO getOpportunityById(Long id) {
        Opportunity opp = opportunityRepository.findByIdWithSkills(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));
        return mapToDTO(opp);
    }

    @Override
    public OpportunityDTO createOpportunity(OpportunityCreateDTO createDTO) {
        Opportunity opp = new Opportunity(
                createDTO.getTitle().trim(),
                createDTO.getCompany().trim(),
                createDTO.getType(),
                createDTO.getLocation().trim(),
                createDTO.getWorkMode()
        );
        opp.setStipendOrSalary(createDTO.getStipendOrSalary());
        opp.setMinCgpa(createDTO.getMinCgpa() != null ? createDTO.getMinCgpa() : 0.0);
        opp.setDeadline(createDTO.getDeadline());
        opp.setDescription(createDTO.getDescription().trim());
        opp.setIsActive(createDTO.getIsActive() != null ? createDTO.getIsActive() : true);

        if (createDTO.getRequiredSkillIds() != null && !createDTO.getRequiredSkillIds().isEmpty()) {
            Set<Skill> skills = new HashSet<>(skillRepository.findAllById(createDTO.getRequiredSkillIds()));
            opp.setRequiredSkills(skills);
        }

        Opportunity saved = opportunityRepository.save(opp);
        return mapToDTO(saved);
    }

    @Override
    public OpportunityDTO updateOpportunity(Long id, OpportunityCreateDTO updateDTO) {
        Opportunity opp = getOpportunityEntity(id);

        opp.setTitle(updateDTO.getTitle().trim());
        opp.setCompany(updateDTO.getCompany().trim());
        opp.setType(updateDTO.getType());
        opp.setLocation(updateDTO.getLocation().trim());
        opp.setWorkMode(updateDTO.getWorkMode());
        opp.setStipendOrSalary(updateDTO.getStipendOrSalary());
        opp.setMinCgpa(updateDTO.getMinCgpa() != null ? updateDTO.getMinCgpa() : 0.0);
        opp.setDeadline(updateDTO.getDeadline());
        opp.setDescription(updateDTO.getDescription().trim());
        if (updateDTO.getIsActive() != null) {
            opp.setIsActive(updateDTO.getIsActive());
        }

        if (updateDTO.getRequiredSkillIds() != null) {
            Set<Skill> skills = new HashSet<>(skillRepository.findAllById(updateDTO.getRequiredSkillIds()));
            opp.setRequiredSkills(skills);
        }

        Opportunity saved = opportunityRepository.save(opp);
        return mapToDTO(saved);
    }

    @Override
    public void deleteOpportunity(Long id) {
        Opportunity opp = getOpportunityEntity(id);
        opportunityRepository.delete(opp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpportunityDTO> searchOpportunities(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getActiveOpportunities();
        }
        return opportunityRepository.searchOpportunities(query.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OpportunityDTO toggleOpportunityStatus(Long id) {
        Opportunity opp = getOpportunityEntity(id);
        opp.setIsActive(!opp.getIsActive());
        return mapToDTO(opportunityRepository.save(opp));
    }

    @Override
    @Transactional(readOnly = true)
    public Opportunity getOpportunityEntity(Long id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));
    }

    private OpportunityDTO mapToDTO(Opportunity opp) {
        OpportunityDTO dto = new OpportunityDTO();
        dto.setId(opp.getId());
        dto.setTitle(opp.getTitle());
        dto.setCompany(opp.getCompany());
        dto.setType(opp.getType());
        dto.setLocation(opp.getLocation());
        dto.setWorkMode(opp.getWorkMode());
        dto.setStipendOrSalary(opp.getStipendOrSalary());
        dto.setMinCgpa(opp.getMinCgpa());
        dto.setDeadline(opp.getDeadline());
        dto.setDescription(opp.getDescription());
        dto.setIsActive(opp.getIsActive());
        dto.setCreatedAt(opp.getCreatedAt());

        if (opp.getRequiredSkills() != null) {
            Set<SkillDTO> skillDTOs = opp.getRequiredSkills().stream()
                    .map(s -> new SkillDTO(s.getId(), s.getName(), s.getCategory(), s.getDescription()))
                    .collect(Collectors.toSet());
            dto.setRequiredSkills(skillDTOs);
        }

        try {
            dto.setApplicantCount(applicationRepository.findByOpportunityId(opp.getId()).size());
        } catch (Exception e) {
            dto.setApplicantCount(0);
        }

        return dto;
    }
}
