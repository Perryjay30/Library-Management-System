package com.maids.cc.librarymanagementsystem.book.repository;

import com.maids.cc.librarymanagementsystem.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findById(Long bookId);
}
