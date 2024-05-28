package org.example.schoolmanagementsystemspring.textbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TextBookRepository extends JpaRepository<TextBook, Integer> {
    @Query("select (count(t) > 0) from TextBook t where t.isbn = ?1")
    boolean existsByIsbn(@NonNull String isbn);

    @Query("select t from TextBook t where t.isbn = ?1")
    Optional<TextBook> findByIsbn(@NonNull String isbn);
}