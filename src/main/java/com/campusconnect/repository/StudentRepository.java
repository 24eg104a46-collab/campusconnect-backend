package com.campusconnect.repository;

import com.campusconnect.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByRollNumber(String rollNumber);
    boolean existsByEmail(String email);
    boolean existsByRollNumber(String rollNumber);
    List<Student> findByDepartment(String department);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.skills WHERE " +
           "LOWER(s.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.department) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Student> searchStudents(@Param("query") String query);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.skills")
    List<Student> findAllWithSkills();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.skills WHERE s.id = :id")
    Optional<Student> findByIdWithSkills(@Param("id") Long id);
}
