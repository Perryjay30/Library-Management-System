package com.maids.cc.librarymanagementsystem.borrowingrecord.service;

import com.maids.cc.librarymanagementsystem.book.model.Book;
import com.maids.cc.librarymanagementsystem.book.service.BookService;
import com.maids.cc.librarymanagementsystem.borrowingrecord.model.BorrowingRecord;
import com.maids.cc.librarymanagementsystem.borrowingrecord.repository.BorrowingRecordRepository;
import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import com.maids.cc.librarymanagementsystem.patron.model.Patron;
import com.maids.cc.librarymanagementsystem.patron.service.PatronService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final PatronService patronService;
    private final BookService bookService;


    @Override
    public Response borrowBook(Long bookId, String emailAddress) {
        Book existingBook = bookService.retrieveBookById(bookId);
        Patron existingPatron = patronService.getExistingPatron(emailAddress);

        BorrowingRecord newRecord = new BorrowingRecord();
        newRecord.setBook(existingBook);
        newRecord.setBorrowerName(existingPatron.getFirstName());
        newRecord.setBookTitle(existingBook.getTitle());
        newRecord.setPatron(existingPatron);
        newRecord.setBorrowingDate(LocalDateTime.now());
        borrowingRecordRepository.save(newRecord);
        return new Response(existingBook.getTitle() + " has been borrowed from the Library by " + existingPatron.getFirstName());
    }

    @Override
    public Response returnBook(Long bookId, String emailAddress) {
        Book existingBook = bookService.retrieveBookById(bookId);
        Patron existingPatron = patronService.getExistingPatron(emailAddress);
        BorrowingRecord existingRecord = borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNull(existingBook.getId(), existingPatron.getId())
                .orElseThrow(() -> new LibraryManagementSystemException("Borrowing record not found"));
        existingRecord.setReturnDate(LocalDateTime.now());
        borrowingRecordRepository.save(existingRecord);
        return new Response(existingBook.getTitle() + " has been returned back to the Library by " + existingPatron.getFirstName());
    }
}
