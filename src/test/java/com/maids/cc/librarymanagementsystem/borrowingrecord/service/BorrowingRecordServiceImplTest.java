package com.maids.cc.librarymanagementsystem.borrowingrecord.service;

import com.maids.cc.librarymanagementsystem.book.model.Book;
import com.maids.cc.librarymanagementsystem.book.service.BookService;
import com.maids.cc.librarymanagementsystem.borrowingrecord.model.BorrowingRecord;
import com.maids.cc.librarymanagementsystem.borrowingrecord.repository.BorrowingRecordRepository;
import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.service.PatronService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BorrowingRecordServiceImplTest {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @MockBean
    private BorrowingRecordRepository borrowingRecordRepository;

    @MockBean
    private PatronService patronService;
    @MockBean
    private BookService bookService;

    @Test
    void testThatPatronCanBorrowBook() {
        Long bookId = 52L;
        String emailAddress = "mrjesus@gmail.com";

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Once upon a time in Japan");

        Patron patron = new Patron();
        patron.setId(1L);
        patron.setFirstName("Olayemi");

        when(bookService.retrieveBookById(bookId)).thenReturn(book);
        when(patronService.getExistingPatron(emailAddress)).thenReturn(patron);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(null);

        Response response = borrowingRecordService.borrowBook(bookId, emailAddress);
        assertEquals("Once upon a time in Japan has been borrowed from the Library by Olayemi", response.getMessage());
    }

    @Test
    void testFailureToBorrowBookWhenBookNotFound() {
        Long bookId = 1L;
        String emailAddress = "mrjesus@gmail.com";

        when(bookService.retrieveBookById(bookId)).thenThrow(new LibraryManagementSystemException("Book isn't available"));

        assertThrows(LibraryManagementSystemException.class, () -> borrowingRecordService.borrowBook(bookId, emailAddress));
    }

    @Test
    void testFailureToBorrowBookWhenPatronNotFound() {
        Long bookId = 1L;
        String emailAddress = "mrjesus@gmail.com";

        Book book = new Book();
        book.setId(bookId);

        when(bookService.retrieveBookById(bookId)).thenReturn(book);
        when(patronService.getExistingPatron(emailAddress)).thenThrow(new LibraryManagementSystemException("Patron isn't available"));

        assertThrows(LibraryManagementSystemException.class, () -> borrowingRecordService.borrowBook(bookId, emailAddress));
    }

    @Test
    void testSuccessfulReturningOfBook() {
        Long bookId = 1L;
        String emailAddress = "mrjesus@gmail.com";

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Once upon a time in Japan");

        Patron patron = new Patron();
        patron.setId(1L);
        patron.setFirstName("Olayemi");

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDateTime.now());

        when(bookService.retrieveBookById(bookId)).thenReturn(book);
        when(patronService.getExistingPatron(emailAddress)).thenReturn(patron);
        when(borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNull(bookId, patron.getId()))
                .thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(null);

        Response response = borrowingRecordService.returnBook(bookId, emailAddress);

        assertEquals("Once upon a time in Japan has been returned back to the Library by Olayemi", response.getMessage());
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void testFailureToReturnBookWhenBookNotFound() {
        Long bookId = 1L;
        String emailAddress = "mrjesus@gmail.com";

        when(bookService.retrieveBookById(bookId)).thenThrow(new LibraryManagementSystemException("Book isn't available"));

        assertThrows(LibraryManagementSystemException.class, () -> borrowingRecordService.returnBook(bookId, emailAddress));
    }

    @Test
    void testFailureToReturnBookWhenPatronNotFound() {
        Long bookId = 1L;
        String emailAddress = "mrjesus@gmail.com";

        Book book = new Book();
        book.setId(bookId);

        when(bookService.retrieveBookById(bookId)).thenReturn(book);
        when(patronService.getExistingPatron(emailAddress)).thenThrow(new LibraryManagementSystemException("Patron isn't available"));

        assertThrows(LibraryManagementSystemException.class, () -> borrowingRecordService.returnBook(bookId, emailAddress));
    }

    @Test
    void testFailureToReturnBookWhenBorrowingRecordNotFound() {
        Long bookId = 1L;
        String emailAddress = "mrjesus@gmail.com";

        Book book = new Book();
        book.setId(bookId);

        Patron patron = new Patron();
        patron.setId(1L);

        when(bookService.retrieveBookById(bookId)).thenReturn(book);
        when(patronService.getExistingPatron(emailAddress)).thenReturn(patron);
        when(borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNull(bookId, patron.getId()))
                .thenReturn(Optional.empty());

        assertThrows(LibraryManagementSystemException.class, () -> borrowingRecordService.returnBook(bookId, emailAddress));
    }
}