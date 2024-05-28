package org.example.schoolmanagementsystemspring.parent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Integer> {
    @Query("select p from Parent p where p.studentResponsible.email = ?1")
    Optional<Parent> findByStudentResponsible_Email(@NonNull String email);
}