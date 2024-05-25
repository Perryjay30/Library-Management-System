package com.maids.cc.librarymanagementsystem.borrowingrecord.service;

import com.maids.cc.librarymanagementsystem.book.dto.request.BorrowBookRequest;
import com.maids.cc.librarymanagementsystem.generalresponse.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BorrowingRecordServiceImplTest {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

//    @Test
//    void testThatPatronCanBorrowBook() {
//        BorrowBookRequest borrowBookRequest = new BorrowBookRequest();
//        borrowBookRequest.setBookId(52L);
//        borrowBookRequest.setEmailAddress("mrjesus3003@gmail.com");
//        Response response = borrowingRecordService.borrowBook(borrowBookRequest);
//        assertEquals("Once upon a time in Japan has been borrowed from the Library by Olayemi", response.getMessage());
//    }
}