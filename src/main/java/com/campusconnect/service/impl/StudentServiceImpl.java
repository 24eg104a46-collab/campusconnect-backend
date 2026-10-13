package com.campusconnect.service.impl;

import com.campusconnect.dto.SkillDTO;
import com.campusconnect.dto.StudentCreateDTO;
import com.campusconnect.dto.StudentDTO;
import com.campusconnect.entity.Skill;
import com.campusconnect.entity.Student;
import com.campusconnect.exception.DuplicateResourceException;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.ApplicationRepository;
import com.campusconnect.repository.SkillRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final ApplicationRepository applicationRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              SkillRepository skillRepository,
                              ApplicationRepository applicationRepository) {
        this.studentRepository = studentRepository;
        this.skillRepository = skillRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAllWithSkills().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findByIdWithSkills(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return mapToDTO(student);
    }

    @Override
    public StudentDTO createStudent(StudentCreateDTO createDTO) {
        if (studentRepository.existsByEmail(createDTO.getEmail().trim().toLowerCase())) {
            throw new DuplicateResourceException("Email already registered: " + createDTO.getEmail());
        }
        if (studentRepository.existsByRollNumber(createDTO.getRollNumber().trim())) {
            throw new DuplicateResourceException("Roll number already registered: " + createDTO.getRollNumber());
        }

        Student student = new Student(
                createDTO.getFullName().trim(),
                createDTO.getEmail().trim().toLowerCase(),
                createDTO.getRollNumber().trim(),
                createDTO.getDepartment().trim(),
                createDTO.getYearOfStudy(),
                createDTO.getCgpa()
        );
        student.setPhone(createDTO.getPhone());
        student.setBio(createDTO.getBio());
        student.setGithubUrl(createDTO.getGithubUrl());
        student.setLinkedinUrl(createDTO.getLinkedinUrl());

        if (createDTO.getSkillIds() != null && !createDTO.getSkillIds().isEmpty()) {
            Set<Skill> skills = new HashSet<>(skillRepository.findAllById(createDTO.getSkillIds()));
            student.setSkills(skills);
        }

        Student saved = studentRepository.save(student);
        return mapToDTO(saved);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentCreateDTO updateDTO) {
        Student student = getStudentEntity(id);

        String newEmail = updateDTO.getEmail().trim().toLowerCase();
        if (!student.getEmail().equalsIgnoreCase(newEmail) && studentRepository.existsByEmail(newEmail)) {
            throw new DuplicateResourceException("Email already registered: " + newEmail);
        }

        String newRoll = updateDTO.getRollNumber().trim();
        if (!student.getRollNumber().equalsIgnoreCase(newRoll) && studentRepository.existsByRollNumber(newRoll)) {
            throw new DuplicateResourceException("Roll number already registered: " + newRoll);
        }

        student.setFullName(updateDTO.getFullName().trim());
        student.setEmail(newEmail);
        student.setRollNumber(newRoll);
        student.setDepartment(updateDTO.getDepartment().trim());
        student.setYearOfStudy(updateDTO.getYearOfStudy());
        student.setCgpa(updateDTO.getCgpa());
        student.setPhone(updateDTO.getPhone());
        student.setBio(updateDTO.getBio());
        student.setGithubUrl(updateDTO.getGithubUrl());
        student.setLinkedinUrl(updateDTO.getLinkedinUrl());

        if (updateDTO.getSkillIds() != null) {
            Set<Skill> skills = new HashSet<>(skillRepository.findAllById(updateDTO.getSkillIds()));
            student.setSkills(skills);
        }

        Student saved = studentRepository.save(student);
        return mapToDTO(saved);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = getStudentEntity(id);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.searchStudents(query.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO addSkillToStudent(Long studentId, Long skillId) {
        Student student = getStudentEntity(idOrFallback(studentId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
        student.addSkill(skill);
        return mapToDTO(studentRepository.save(student));
    }

    @Override
    public StudentDTO removeSkillFromStudent(Long studentId, Long skillId) {
        Student student = getStudentEntity(idOrFallback(studentId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
        student.removeSkill(skill);
        return mapToDTO(studentRepository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Long idOrFallback(Long id) {
        return id;
    }

    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setRollNumber(student.getRollNumber());
        dto.setDepartment(student.getDepartment());
        dto.setYearOfStudy(student.getYearOfStudy());
        dto.setCgpa(student.getCgpa());
        dto.setPhone(student.getPhone());
        dto.setBio(student.getBio());
        dto.setGithubUrl(student.getGithubUrl());
        dto.setLinkedinUrl(student.getLinkedinUrl());
        dto.setCreatedAt(student.getCreatedAt());

        if (student.getSkills() != null) {
            Set<SkillDTO> skillDTOs = student.getSkills().stream()
                    .map(s -> new SkillDTO(s.getId(), s.getName(), s.getCategory(), s.getDescription()))
                    .collect(Collectors.toSet());
            dto.setSkills(skillDTOs);
        }

        try {
            dto.setApplicationCount(applicationRepository.findByStudentId(student.getId()).size());
        } catch (Exception e) {
            dto.setApplicationCount(0);
        }

        return dto;
    }
}
