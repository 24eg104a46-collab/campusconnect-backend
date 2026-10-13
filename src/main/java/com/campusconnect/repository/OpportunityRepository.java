package com.campusconnect.repository;

import com.campusconnect.entity.Opportunity;
import com.campusconnect.entity.OpportunityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findByIsActiveTrue();
    List<Opportunity> findByType(OpportunityType type);

    @Query("SELECT DISTINCT o FROM Opportunity o LEFT JOIN FETCH o.requiredSkills WHERE o.isActive = true")
    List<Opportunity> findAllActiveWithSkills();

    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.requiredSkills WHERE o.id = :id")
    Optional<Opportunity> findByIdWithSkills(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Opportunity o LEFT JOIN FETCH o.requiredSkills WHERE " +
           "(LOWER(o.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(o.company) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(o.location) LIKE LOWER(CONCAT('%', :query, '%'))) AND o.isActive = true")
    List<Opportunity> searchOpportunities(@Param("query") String query);
}
