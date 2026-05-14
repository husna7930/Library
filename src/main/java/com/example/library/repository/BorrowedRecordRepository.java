package com.example.library.repository;

import com.example.library.model.BorrowedRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowedRecordRepository extends JpaRepository<BorrowedRecord, Integer> {

     List<BorrowedRecord> findByBorrowerId(Integer borrowerId);

     List<BorrowedRecord> findByBookRecordId(Integer bookRecordId);

     boolean existsByBorrowerIdAndBookId(Integer borrowerId, Integer bookId);

}
