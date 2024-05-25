package com.maids.cc.librarymanagementsystem.book.controller;

import com.maids.cc.librarymanagementsystem.book.dto.request.AddBookRequest;
import com.maids.cc.librarymanagementsystem.book.dto.request.EditBookRequest;
import com.maids.cc.librarymanagementsystem.book.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/addBooksToLibrary")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> addBooksToLibrary(@Valid @RequestBody AddBookRequest addBookRequest) {
        return ResponseEntity.ok(bookService.addBookToLibrary(addBookRequest));
    }

    @GetMapping("/getBookById/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> retrieveBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.retrieveBookById(bookId));
    }

    @PutMapping("/editBookDetails/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> editBookDetails(@PathVariable Long bookId, @Valid @RequestBody EditBookRequest editBookRequest) {
        return ResponseEntity.ok(bookService.editBookDetails(bookId, editBookRequest));
    }

    @GetMapping("/getAllBooks")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(bookService.retrieveAllBooks());
    }

    @DeleteMapping("/removeBook/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteBookFromLibrary(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.removeBookFromLibrary(bookId));
    }
}
