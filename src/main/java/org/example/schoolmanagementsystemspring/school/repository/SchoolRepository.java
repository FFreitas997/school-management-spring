package org.example.schoolmanagementsystemspring.school.repository;

import org.example.schoolmanagementsystemspring.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

    @Query("select (count(s) > 0) from School s where s.email = ?1")
    boolean existsByEmail(@NonNull String email);
}