package org.example.schoolmanagementsystemspring.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("select e from Event e where e.school.id = ?1")
    Page<Event> findBySchool_Id(@NonNull Integer id, Pageable pageable);

    @Query("select e from Event e where e.school.id = ?1")
    List<Event> findBySchoolID(@NonNull Integer id);
}