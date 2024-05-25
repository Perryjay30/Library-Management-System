package com.maids.cc.librarymanagementsystem.borrowingrecord.repository;

import com.maids.cc.librarymanagementsystem.borrowingrecord.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    @Query(value = "SELECT * FROM borrowing_record br WHERE br.book_id = :bookId AND br.patron_id = :patronId AND br.return_date IS NULL", nativeQuery = true)
    Optional<BorrowingRecord> findBorrowingRecordByBookAndPatronAndReturnDateIsNull(@Param("bookId") Long bookId, @Param("patronId") Long patronId);
}
