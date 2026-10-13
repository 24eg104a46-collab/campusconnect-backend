package com.campusconnect.service;

import com.campusconnect.dto.StudentCreateDTO;
import com.campusconnect.dto.StudentDTO;
import com.campusconnect.entity.Student;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(Long id);
    StudentDTO createStudent(StudentCreateDTO createDTO);
    StudentDTO updateStudent(Long id, StudentCreateDTO updateDTO);
    void deleteStudent(Long id);
    List<StudentDTO> searchStudents(String query);
    StudentDTO addSkillToStudent(Long studentId, Long skillId);
    StudentDTO removeSkillFromStudent(Long studentId, Long skillId);
    Student getStudentEntity(Long id);
}
