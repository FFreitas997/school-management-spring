package org.example.schoolmanagementsystemspring.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, AssignmentID> {
}