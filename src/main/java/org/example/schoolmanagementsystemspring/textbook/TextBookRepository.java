package org.example.schoolmanagementsystemspring.textbook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextBookRepository extends JpaRepository<TextBook, Integer> {
}