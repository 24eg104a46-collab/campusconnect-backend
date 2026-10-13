package com.campusconnect.service.impl;

import com.campusconnect.dto.SkillDTO;
import com.campusconnect.entity.Skill;
import com.campusconnect.entity.SkillCategory;
import com.campusconnect.exception.DuplicateResourceException;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.SkillRepository;
import com.campusconnect.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDTO getSkillById(Long id) {
        Skill skill = getSkillEntity(id);
        return mapToDTO(skill);
    }

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        if (skillRepository.existsByNameIgnoreCase(skillDTO.getName().trim())) {
            throw new DuplicateResourceException("Skill with name '" + skillDTO.getName() + "' already exists");
        }
        Skill skill = new Skill(
                skillDTO.getName().trim(),
                skillDTO.getCategory(),
                skillDTO.getDescription()
        );
        Skill savedSkill = skillRepository.save(skill);
        return mapToDTO(savedSkill);
    }

    @Override
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        Skill skill = getSkillEntity(id);
        if (!skill.getName().equalsIgnoreCase(skillDTO.getName().trim()) &&
                skillRepository.existsByNameIgnoreCase(skillDTO.getName().trim())) {
            throw new DuplicateResourceException("Skill with name '" + skillDTO.getName() + "' already exists");
        }
        skill.setName(skillDTO.getName().trim());
        skill.setCategory(skillDTO.getCategory());
        skill.setDescription(skillDTO.getDescription());
        return mapToDTO(skillRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        Skill skill = getSkillEntity(id);
        skillRepository.delete(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> getSkillsByCategory(SkillCategory category) {
        return skillRepository.findByCategory(category).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Skill getSkillEntity(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
    }

    private SkillDTO mapToDTO(Skill skill) {
        return new SkillDTO(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                skill.getDescription()
        );
    }
}
