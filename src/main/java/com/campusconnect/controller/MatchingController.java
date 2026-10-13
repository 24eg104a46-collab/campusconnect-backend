package com.campusconnect.controller;

import com.campusconnect.dto.ApiResponse;
import com.campusconnect.dto.SkillMatchResultDTO;
import com.campusconnect.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<SkillMatchResultDTO>>> matchStudentWithOpportunities(
            @PathVariable Long studentId) {
        List<SkillMatchResultDTO> matches = matchingService.matchStudentWithOpportunities(studentId);
        return ResponseEntity.ok(ApiResponse.success("Matching opportunities calculated", matches));
    }

    @GetMapping("/opportunity/{opportunityId}")
    public ResponseEntity<ApiResponse<List<SkillMatchResultDTO>>> matchOpportunityWithStudents(
            @PathVariable Long opportunityId) {
        List<SkillMatchResultDTO> matches = matchingService.matchOpportunityWithStudents(opportunityId);
        return ResponseEntity.ok(ApiResponse.success("Matching student candidates calculated", matches));
    }

    @GetMapping("/compare")
    public ResponseEntity<ApiResponse<SkillMatchResultDTO>> compare(
            @RequestParam Long studentId,
            @RequestParam Long opportunityId) {
        SkillMatchResultDTO match = matchingService.getMatchDetails(studentId, opportunityId);
        return ResponseEntity.ok(ApiResponse.success("Direct match result calculated", match));
    }
}
