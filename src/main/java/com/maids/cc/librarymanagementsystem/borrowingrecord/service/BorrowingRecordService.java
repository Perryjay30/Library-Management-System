package com.maids.cc.librarymanagementsystem.borrowingrecord.service;

import com.maids.cc.librarymanagementsystem.generalresponse.Response;

public interface BorrowingRecordService {
    Response borrowBook(Long bookId, String emailAddress);
    Response returnBook(Long bookId, String emailAddress);
}
