package com.campusconnect.controller;

import com.campusconnect.dto.ApiResponse;
import com.campusconnect.dto.OpportunityCreateDTO;
import com.campusconnect.dto.OpportunityDTO;
import com.campusconnect.service.OpportunityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OpportunityDTO>>> getAllOpportunities(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<OpportunityDTO> opportunities;
        if (query != null && !query.trim().isEmpty()) {
            opportunities = opportunityService.searchOpportunities(query);
        } else if (all) {
            opportunities = opportunityService.getAllOpportunities();
        } else {
            opportunities = opportunityService.getActiveOpportunities();
        }
        return ResponseEntity.ok(ApiResponse.success("Opportunities retrieved successfully", opportunities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityDTO>> getOpportunityById(@PathVariable Long id) {
        OpportunityDTO opportunity = opportunityService.getOpportunityById(id);
        return ResponseEntity.ok(ApiResponse.success("Opportunity retrieved successfully", opportunity));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OpportunityDTO>> createOpportunity(
            @Valid @RequestBody OpportunityCreateDTO createDTO) {
        OpportunityDTO created = opportunityService.createOpportunity(createDTO);
        return new ResponseEntity<>(ApiResponse.success("Opportunity posted successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityDTO>> updateOpportunity(
            @PathVariable Long id,
            @Valid @RequestBody OpportunityCreateDTO updateDTO) {
        OpportunityDTO updated = opportunityService.updateOpportunity(id, updateDTO);
        return ResponseEntity.ok(ApiResponse.success("Opportunity updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOpportunity(@PathVariable Long id) {
        opportunityService.deleteOpportunity(id);
        return ResponseEntity.ok(ApiResponse.success("Opportunity deleted successfully", null));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<OpportunityDTO>> toggleStatus(@PathVariable Long id) {
        OpportunityDTO updated = opportunityService.toggleOpportunityStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Opportunity status updated successfully", updated));
    }
}
