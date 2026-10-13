package com.campusconnect.repository;

import com.campusconnect.entity.Application;
import com.campusconnect.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByOpportunityId(Long opportunityId);
    boolean existsByStudentIdAndOpportunityId(Long studentId, Long opportunityId);
    Optional<Application> findByStudentIdAndOpportunityId(Long studentId, Long opportunityId);
    List<Application> findByStatus(ApplicationStatus status);
    long countByStatus(ApplicationStatus status);

    @Query("SELECT a FROM Application a JOIN FETCH a.student s JOIN FETCH a.opportunity o ORDER BY a.appliedDate DESC")
    List<Application> findAllWithDetails();

    @Query("SELECT a FROM Application a JOIN FETCH a.student s JOIN FETCH a.opportunity o WHERE a.id = :id")
    Optional<Application> findByIdWithDetails(@Param("id") Long id);
}
