package com.campusconnect.controller;

import com.campusconnect.dto.ApiResponse;
import com.campusconnect.dto.ApplicationCreateDTO;
import com.campusconnect.dto.ApplicationDTO;
import com.campusconnect.dto.ApplicationStatusUpdateDTO;
import com.campusconnect.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationDTO>>> getAllApplications() {
        List<ApplicationDTO> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationDTO>> getApplicationById(@PathVariable Long id) {
        ApplicationDTO app = applicationService.getApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success("Application retrieved successfully", app));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<ApplicationDTO>>> getApplicationsByStudent(@PathVariable Long studentId) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student applications retrieved successfully", applications));
    }

    @GetMapping("/opportunity/{opportunityId}")
    public ResponseEntity<ApiResponse<List<ApplicationDTO>>> getApplicationsByOpportunity(@PathVariable Long opportunityId) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByOpportunity(opportunityId);
        return ResponseEntity.ok(ApiResponse.success("Opportunity applications retrieved successfully", applications));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationDTO>> apply(@Valid @RequestBody ApplicationCreateDTO createDTO) {
        ApplicationDTO created = applicationService.apply(createDTO);
        return new ResponseEntity<>(ApiResponse.success("Application submitted successfully", created), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationDTO>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateDTO statusDTO) {
        ApplicationDTO updated = applicationService.updateStatus(id, statusDTO);
        return ResponseEntity.ok(ApiResponse.success("Application status updated to " + statusDTO.getStatus(), updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(@PathVariable Long id) {
        applicationService.withdrawApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn successfully", null));
    }
}
