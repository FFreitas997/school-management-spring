package org.example.schoolmanagementsystemspring.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Page<Course> findByTeacher_EmailAndTeacher_IsEnabledTrue(String email, Pageable pageable);

    @Query("select c from Course c where c.courseCode = ?1")
    Optional<Course> findByCourseCode(@NonNull String courseCode);

    @Query("select c from Course c inner join c.students students where students.email = ?1")
    Page<Course> findByStudents_Email(@NonNull String email, Pageable pageable);

    @Query("select (count(c) > 0) from Course c where upper(c.courseCode) = upper(?1)")
    boolean existsByCourseCodeIgnoreCase(@NonNull String courseCode);

    @Query("select (count(c) > 0) from Course c where c.courseCode = ?1")
    boolean existsByCourseCode(@NonNull String courseCode);
}