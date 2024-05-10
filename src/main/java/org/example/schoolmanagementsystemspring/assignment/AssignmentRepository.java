package org.example.schoolmanagementsystemspring.assignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, AssignmentID> {


    @Query("""
            select (count(a) > 0) from Assignment a
            where a.id.courseCode = ?1 and a.id.deliverAssignment = ?2 and a.enabled = true""")
    boolean existsById_CourseCodeAndId_DeliverAssignmentAndEnabledTrue(String courseCode, LocalDateTime deliverAssignment);

    @Query("select a from Assignment a where a.id.courseCode = ?1 and a.id.deliverAssignment = ?2")
    List<Assignment> findById_CourseCodeAndId_DeliverAssignment(@NonNull String courseCode, @NonNull LocalDateTime deliverAssignment);

    @Query("select a from Assignment a where a.id.courseCode = ?1 and a.id.deliverAssignment = ?2 and a.enabled = true")
    Page<Assignment> findById_CourseCodeAndId_DeliverAssignmentAndEnabledTrue(String courseCode, LocalDateTime deliverAssignment, Pageable pageable);
}