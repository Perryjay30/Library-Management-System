package com.maids.cc.librarymanagementsystem.book.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.cc.librarymanagementsystem.book.dto.request.AddBookRequest;
import com.maids.cc.librarymanagementsystem.book.dto.request.EditBookRequest;
import com.maids.cc.librarymanagementsystem.book.model.Book;
import com.maids.cc.librarymanagementsystem.book.repository.BookRepository;
import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Response addBookToLibrary(AddBookRequest addBookRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        Book newBook = objectMapper.convertValue(addBookRequest, Book.class);
        newBook.setDateAdded(LocalDateTime.now());
        bookRepository.save(newBook);
        return new Response("Book added successfully to library");
    }

    @Override
    public Book retrieveBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new LibraryManagementSystemException("Book isn't available"));
    }

    @Override
    public Response editBookDetails(Long bookId, EditBookRequest editBookRequest) {
        Book existingBook = retrieveBookById(bookId);
        existingBook.setAuthor(editBookRequest.getAuthor() != null && !editBookRequest.getAuthor().equals(" ")
                ? editBookRequest.getAuthor() : existingBook.getAuthor());
        existingBook.setTitle(editBookRequest.getTitle() != null && !editBookRequest.getTitle()
                .equals(" ") ? editBookRequest.getTitle() : existingBook.getTitle());
        existingBook.setPublicationYear(editBookRequest.getPublicationYear() != null && !editBookRequest.getPublicationYear().equals(" ")
                ? editBookRequest.getPublicationYear() : existingBook.getPublicationYear());
        existingBook.setISBN(editBookRequest.getISBN() != null && !editBookRequest.getISBN()
                .equals(" ") ? editBookRequest.getISBN() : existingBook.getISBN());
        existingBook.setDateModified(LocalDateTime.now());
        bookRepository.save(existingBook);
        return new Response("Book Details updated successfully");
    }

    @Override
    public List<Book> retrieveAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Response removeBookFromLibrary(Long bookId) {
        Book existingBook = retrieveBookById(bookId);
        bookRepository.delete(existingBook);
        return new Response("Book removed successfully from the library");
    }
}
