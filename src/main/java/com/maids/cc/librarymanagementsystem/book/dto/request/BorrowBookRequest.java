package com.maids.cc.librarymanagementsystem.book.dto.request;

import lombok.Data;

@Data
public class BorrowBookRequest {
    private Long bookId;
    private String emailAddress;
}
