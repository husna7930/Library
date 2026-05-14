package com.example.library.service;

import com.example.library.dto.BookRecordDTO;
import com.example.library.model.Book;
import com.example.library.model.BookRecord;
import com.example.library.repository.BookRecordRepository;
import com.example.library.repository.BookRepository;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookRecordService {

    private final BookRecordRepository bookRecordRepository;
    private final BookRepository bookRepository;

    public BookRecordService(BookRecordRepository bookRecordRepository, BookRepository bookRepository) {
        this.bookRecordRepository = bookRecordRepository;
        this.bookRepository = bookRepository;
    }

    public BookRecordDTO registerCopy(Integer bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        BookRecord records = new BookRecord();
        records.setBook(book);
        records.setIsbn(book.getIsbn());
        records.setTitle(book.getTitle());
        records.setAuthor(book.getAuthor());
        records.setStatus("AVAILABLE");

        BookRecord savedRecord = bookRecordRepository.save(records);
        return toDTO(savedRecord);
    }

    public List<BookRecord> getRecordsByIsbn(String isbn) {
        return bookRecordRepository.findByIsbn(isbn);
    }

    public List<BookRecordDTO> getAllBookRecords() {
        List<BookRecord> records = bookRecordRepository.findAll();
        return records.stream().map(this::toDTO).toList();
    }

    public void deleteCopies(Integer bookRecordId) {

        if (!bookRecordRepository.existsById(bookRecordId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist");
        }

        bookRecordRepository.deleteById(bookRecordId);
    }

    public BookRecordDTO toDTO(BookRecord bookRecord) {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookRecordId(bookRecord.getBookRecordId());
        dto.setIsbn(bookRecord.getIsbn());
        dto.setTitle(bookRecord.getTitle());
        dto.setAuthor(bookRecord.getAuthor());
        dto.setStatus(bookRecord.getStatus());
        dto.setBookId(bookRecord.getBook().getId());
        dto.setBorrowerId(bookRecord.getBorrowerId());
        return dto;
    }
}
