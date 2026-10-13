package com.campusconnect.service.impl;

import com.campusconnect.dto.DashboardStatsDTO;
import com.campusconnect.dto.SkillDTO;
import com.campusconnect.entity.ApplicationStatus;
import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.Student;
import com.campusconnect.repository.ApplicationRepository;
import com.campusconnect.repository.OpportunityRepository;
import com.campusconnect.repository.SkillRepository;
import com.campusconnect.repository.StudentRepository;
import com.campusconnect.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;
    private final SkillRepository skillRepository;

    public DashboardServiceImpl(StudentRepository studentRepository,
                                OpportunityRepository opportunityRepository,
                                ApplicationRepository applicationRepository,
                                SkillRepository skillRepository) {
        this.studentRepository = studentRepository;
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        List<Student> students = studentRepository.findAll();
        List<Opportunity> opportunities = opportunityRepository.findAll();
        long totalApps = applicationRepository.count();
        long totalSkills = skillRepository.count();

        stats.setTotalStudents(students.size());
        stats.setTotalOpportunities(opportunities.size());
        stats.setTotalApplications(totalApps);
        stats.setTotalSkills(totalSkills);

        double avgCgpa = students.stream()
                .mapToDouble(Student::getCgpa)
                .average()
                .orElse(0.0);
        stats.setAverageCgpa(Math.round(avgCgpa * 100.0) / 100.0);

        Map<String, Long> statusMap = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusMap.put(status.name(), applicationRepository.countByStatus(status));
        }
        stats.setApplicationsByStatus(statusMap);

        Map<String, Long> oppTypeMap = opportunities.stream()
                .collect(Collectors.groupingBy(o -> o.getType().name(), Collectors.counting()));
        stats.setOpportunitiesByType(oppTypeMap);

        Map<Long, Long> skillDemandCount = new HashMap<>();
        for (Opportunity opp : opportunities) {
            if (opp.getRequiredSkills() != null) {
                opp.getRequiredSkills().forEach(s ->
                        skillDemandCount.put(s.getId(), skillDemandCount.getOrDefault(s.getId(), 0L) + 1));
            }
        }

        List<SkillDTO> topSkills = skillDemandCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(6)
                .map(e -> skillRepository.findById(e.getKey()).map(s -> {
                    SkillDTO dto = new SkillDTO(s.getId(), s.getName(), s.getCategory(), s.getDescription());
                    dto.setOpportunityCount(e.getValue());
                    return dto;
                }).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        stats.setTopInDemandSkills(topSkills);
        return stats;
    }
}
