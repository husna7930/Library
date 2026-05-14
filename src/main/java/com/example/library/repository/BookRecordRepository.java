package com.example.library.repository;

import com.example.library.model.BookRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRecordRepository extends JpaRepository<BookRecord, Integer> {

    List<BookRecord> findByIsbn(String isbn);

    List<BookRecord> findByBookId(Integer bookId);

    void deleteByBookId(Integer bookId);

}
