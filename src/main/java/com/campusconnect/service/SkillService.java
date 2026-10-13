package com.campusconnect.service;

import com.campusconnect.dto.SkillDTO;
import com.campusconnect.entity.Skill;
import com.campusconnect.entity.SkillCategory;

import java.util.List;

public interface SkillService {
    List<SkillDTO> getAllSkills();
    SkillDTO getSkillById(Long id);
    SkillDTO createSkill(SkillDTO skillDTO);
    SkillDTO updateSkill(Long id, SkillDTO skillDTO);
    void deleteSkill(Long id);
    List<SkillDTO> getSkillsByCategory(SkillCategory category);
    Skill getSkillEntity(Long id);
}
