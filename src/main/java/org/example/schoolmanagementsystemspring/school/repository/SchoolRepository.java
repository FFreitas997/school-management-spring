package org.example.schoolmanagementsystemspring.school.repository;

import org.example.schoolmanagementsystemspring.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Integer> {
}