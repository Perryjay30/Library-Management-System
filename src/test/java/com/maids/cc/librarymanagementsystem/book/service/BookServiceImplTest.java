package com.maids.cc.librarymanagementsystem.book.service;

import com.maids.cc.librarymanagementsystem.book.dto.request.AddBookRequest;
import com.maids.cc.librarymanagementsystem.book.dto.request.EditBookRequest;
import com.maids.cc.librarymanagementsystem.book.model.Book;
import com.maids.cc.librarymanagementsystem.book.repository.BookRepository;
import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@EnableCaching
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("books").clear();
        cacheManager.getCache("book").clear();
    }

    @Test
    void testThatBooksCanBeAddedToTheLibrary() {
        AddBookRequest addBookRequest = new AddBookRequest();
        addBookRequest.setTitle("Love is a beautiful thing");
        addBookRequest.setAuthor("Bruce Willis");
        addBookRequest.setISBN("987-5-90-321567-0");
        addBookRequest.setPublicationYear("2003");

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Response response = bookService.addBookToLibrary(addBookRequest);
        assertEquals("Book added successfully to library", response.getMessage());
    }

    @Test
    void testRetrieveBookById_Success() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book retrievedBook = bookService.retrieveBookById(bookId);
        assertEquals(bookId, retrievedBook.getId());

        Book retrievedBook2 = bookService.retrieveBookById(bookId);
        assertEquals(bookId, retrievedBook2.getId());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testRetrieveBookById_NotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(LibraryManagementSystemException.class, () -> bookService.retrieveBookById(bookId));
    }

    @Test
    void testEditBookDetails_Success() {
        Long bookId = 1L;
        EditBookRequest editBookRequest = new EditBookRequest();
        editBookRequest.setTitle("Half of a yellow sun");
        editBookRequest.setAuthor("Jade Kittens");
        editBookRequest.setISBN("987-5-90-321567-1");
        editBookRequest.setPublicationYear("2022");

        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Response response = bookService.editBookDetails(bookId, editBookRequest);
        assertEquals("Book Details updated successfully", response.getMessage());
    }

    @Test
    void testEditBookDetails_BookNotFound() {
        Long bookId = 1L;
        EditBookRequest editBookRequest = new EditBookRequest();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(LibraryManagementSystemException.class, () -> bookService.editBookDetails(bookId, editBookRequest));
    }

    @Test
    void testRetrieveAllBooks() {
        Book book1 = new Book();
        book1.setTitle("All that Glitters");

        Book book2 = new Book();
        book2.setTitle("Learn Coding");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> books = bookService.retrieveAllBooks();
        assertEquals(2, books.size());

        List<Book> books2 = bookService.retrieveAllBooks();
        assertEquals(2, books2.size());
        verify(bookRepository, times(1)).findAll();
        assertEquals("All that Glitters", books.get(0).getTitle());
        assertEquals("Learn Coding", books.get(1).getTitle());
    }

    @Test
    void testRemoveBookFromLibrary_Success() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        Response response = bookService.removeBookFromLibrary(bookId);
        assertEquals("Book removed successfully from the library", response.getMessage());
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testRemoveBookFromLibrary_BookNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(LibraryManagementSystemException.class, () -> bookService.removeBookFromLibrary(bookId));
    }
}
