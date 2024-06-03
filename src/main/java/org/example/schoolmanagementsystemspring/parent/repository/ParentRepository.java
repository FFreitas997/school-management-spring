package org.example.schoolmanagementsystemspring.parent.repository;

import org.example.schoolmanagementsystemspring.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Integer> {
    @Query("select p from Parent p where p.studentResponsible.email = ?1")
    Optional<Parent> findByStudentResponsible_Email(@NonNull String email);

    @Query("select (count(p) > 0) from Parent p where p.email = ?1")
    boolean existsByEmail(@NonNull String email);

    @Query("select p from Parent p where p.email = ?1 and p.isEnabled = true")
    Optional<Parent> findByEmailAndIsEnabledTrue(@NonNull String email);
}