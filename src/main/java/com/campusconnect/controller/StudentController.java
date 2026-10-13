package com.campusconnect.controller;

import com.campusconnect.dto.ApiResponse;
import com.campusconnect.dto.StudentCreateDTO;
import com.campusconnect.dto.StudentDTO;
import com.campusconnect.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents(
            @RequestParam(required = false) String query) {
        List<StudentDTO> students;
        if (query != null && !query.trim().isEmpty()) {
            students = studentService.searchStudents(query);
        } else {
            students = studentService.getAllStudents();
        }
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(
            @Valid @RequestBody StudentCreateDTO createDTO) {
        StudentDTO created = studentService.createStudent(createDTO);
        return new ResponseEntity<>(ApiResponse.success("Student registered successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentCreateDTO updateDTO) {
        StudentDTO updated = studentService.updateStudent(id, updateDTO);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }

    @PostMapping("/{id}/skills/{skillId}")
    public ResponseEntity<ApiResponse<StudentDTO>> addSkillToStudent(
            @PathVariable Long id,
            @PathVariable Long skillId) {
        StudentDTO updated = studentService.addSkillToStudent(id, skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill added to student successfully", updated));
    }

    @DeleteMapping("/{id}/skills/{skillId}")
    public ResponseEntity<ApiResponse<StudentDTO>> removeSkillFromStudent(
            @PathVariable Long id,
            @PathVariable Long skillId) {
        StudentDTO updated = studentService.removeSkillFromStudent(id, skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill removed from student successfully", updated));
    }
}
