package com.maids.cc.librarymanagementsystem.borrowingrecord.controller;

import com.maids.cc.librarymanagementsystem.borrowingrecord.service.BorrowingRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowingRecord")
public class BorrowingRecordController {
    private final BorrowingRecordService borrowingRecordService;

    public BorrowingRecordController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/borrowBook/{bookId}/patron/{emailAddress}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @PathVariable String emailAddress) {
        return ResponseEntity.ok(borrowingRecordService.borrowBook(bookId, emailAddress));
    }

    @PutMapping("/returnBook/{bookId}/{emailAddress}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId, @PathVariable String emailAddress) {
        return ResponseEntity.ok(borrowingRecordService.returnBook(bookId, emailAddress));
    }
}
