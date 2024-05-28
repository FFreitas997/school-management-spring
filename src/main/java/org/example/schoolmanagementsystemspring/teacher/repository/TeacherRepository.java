package org.example.schoolmanagementsystemspring.teacher.repository;

import org.example.schoolmanagementsystemspring.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("select (count(t) > 0) from Teacher t where upper(t.email) = upper(?1)")
    boolean existsTeacherByEmail(String email);

    @Query("select t from Teacher t where t.email = ?1 and t.isEnabled = true")
    Optional<Teacher> findByEmailAndIsEnabledTrue(@NonNull String email);

    @Query("""
            select t from Teacher t inner join t.studentResponsibleFor studentResponsibleFor
            where studentResponsibleFor.email = ?1""")
    Optional<Teacher> findByStudentResponsibleFor_Email(@NonNull String email);
}