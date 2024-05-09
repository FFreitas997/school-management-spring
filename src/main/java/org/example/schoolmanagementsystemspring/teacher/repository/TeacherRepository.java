package org.example.schoolmanagementsystemspring.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("select (count(t) > 0) from Teacher t where upper(t.email) = upper(?1)")
    boolean existsTeacherByEmail(String email);
}