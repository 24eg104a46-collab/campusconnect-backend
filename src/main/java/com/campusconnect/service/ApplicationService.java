package com.campusconnect.service;

import com.campusconnect.dto.ApplicationCreateDTO;
import com.campusconnect.dto.ApplicationDTO;
import com.campusconnect.dto.ApplicationStatusUpdateDTO;
import com.campusconnect.entity.Application;
import com.campusconnect.entity.ApplicationStatus;

import java.util.List;

public interface ApplicationService {
    ApplicationDTO apply(ApplicationCreateDTO createDTO);
    List<ApplicationDTO> getAllApplications();
    ApplicationDTO getApplicationById(Long id);
    List<ApplicationDTO> getApplicationsByStudent(Long studentId);
    List<ApplicationDTO> getApplicationsByOpportunity(Long opportunityId);
    ApplicationDTO updateStatus(Long id, ApplicationStatusUpdateDTO statusDTO);
    void withdrawApplication(Long id);
    Application getApplicationEntity(Long id);
}
