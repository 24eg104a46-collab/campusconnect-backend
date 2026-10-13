package com.campusconnect.service;

import com.campusconnect.dto.OpportunityCreateDTO;
import com.campusconnect.dto.OpportunityDTO;
import com.campusconnect.entity.Opportunity;

import java.util.List;

public interface OpportunityService {
    List<OpportunityDTO> getAllOpportunities();
    List<OpportunityDTO> getActiveOpportunities();
    OpportunityDTO getOpportunityById(Long id);
    OpportunityDTO createOpportunity(OpportunityCreateDTO createDTO);
    OpportunityDTO updateOpportunity(Long id, OpportunityCreateDTO updateDTO);
    void deleteOpportunity(Long id);
    List<OpportunityDTO> searchOpportunities(String query);
    OpportunityDTO toggleOpportunityStatus(Long id);
    Opportunity getOpportunityEntity(Long id);
}
