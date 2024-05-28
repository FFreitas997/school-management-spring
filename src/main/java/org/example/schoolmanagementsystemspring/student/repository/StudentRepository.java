package org.example.schoolmanagementsystemspring.student.repository;

import org.example.schoolmanagementsystemspring.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("select s from Student s where s.email = ?1 and s.isEnabled = true")
    Optional<Student> findByEmailAndIsEnabledTrue(@NonNull String email);

    @Query("""
            select s from Student s
            where (?1 is null or upper(s.firstName) like upper(concat(?1, '%'))) and (?2 is null or upper(s.lastName) like upper(concat(?2, '%'))) and (?3 is null or upper(s.email) like upper(concat(?3, '%')))""")
    Page<Student> searchStudent(@Nullable String firstName, @Nullable String lastName, @Nullable String email, Pageable pageable);

    @Query("select s from Student s where s.email in ?1")
    List<Student> findByEmailIn(@NonNull Collection<String> emails);

    @Query("select s from Student s inner join s.courses courses where courses.courseCode = ?1")
    Page<Student> findByCourses_CourseCode(@NonNull String courseCode, Pageable pageable);

    @Query("select s from Student s inner join s.courses courses where courses.courseCode = ?1")
    List<Student> findStudentsByCourseCode(@NonNull String courseCode);

    @Query("select (count(s) > 0) from Student s where s.email = ?1")
    boolean existsByEmail(@NonNull String email);
}