package com.example.library.service;

import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BorrowedRecordDTO;
import com.example.library.model.BookRecord;
import com.example.library.model.BorrowedRecord;
import com.example.library.repository.BookRecordRepository;
import com.example.library.repository.BorrowedRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowedRecordService {

    private final BorrowedRecordRepository borrowedRecordRepository;

    private final BookRecordRepository bookRecordRepository;

    public BorrowedRecordService(BorrowedRecordRepository borrowedRecordRepository,
            BookRecordRepository bookRecordRepository) {
        this.borrowedRecordRepository = borrowedRecordRepository;
        this.bookRecordRepository = bookRecordRepository;
    }

    @Transactional
    public BookRecordDTO registerBookRent(Integer borrowerId, Integer bookRecordId) {

        // Check if the book record exists and is available for renting
        BookRecord bookRecord = bookRecordRepository.findById(bookRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Record not found"));

        // Check if the book is already borrowed
        if ("BORROWED".equalsIgnoreCase(bookRecord.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book is not available for renting");
        }

        // Check if borrower already has another copy of the same book
        boolean alreadyBorrowedSameBook = borrowedRecordRepository
                .existsByBorrowerIdAndBookIdAndReturnDateIsNull(borrowerId, bookRecord.getBook().getId());

        if (alreadyBorrowedSameBook) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Borrower already has a copy of this book");
        }

        BorrowedRecord recordsBorrowedRecord = new BorrowedRecord();
        recordsBorrowedRecord.setBorrowerId(borrowerId);
        recordsBorrowedRecord.setBookRecordId(bookRecordId);
        recordsBorrowedRecord.setBorrowDate(LocalDate.now());
        recordsBorrowedRecord.setDueDate(LocalDate.now().plusDays(14));
        recordsBorrowedRecord.setReturnDate(null);

        bookRecord.setStatus("BORROWED");
        bookRecord.setBorrowerId(borrowerId);
        bookRecordRepository.save(bookRecord);

        borrowedRecordRepository.save(recordsBorrowedRecord);

        return toBookRecordDTO(bookRecord);
    }

    public BookRecordDTO returnBook(Integer borrowedRecordId) {
        BorrowedRecord recordsBorrowedRecord = borrowedRecordRepository.findById(borrowedRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrowed Record not found"));

        recordsBorrowedRecord.setReturnDate(LocalDate.now());

        BookRecord bookRecord = bookRecordRepository.findById(recordsBorrowedRecord.getBookRecordId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Record not found"));

        bookRecord.setStatus("AVAILABLE");
        bookRecord.setBorrowerId(null);
        bookRecordRepository.save(bookRecord);

        borrowedRecordRepository.save(recordsBorrowedRecord);

        return toBookRecordDTO(bookRecord);
    }

    public List<BorrowedRecordDTO> getAllBorrowedRecords() {
        List<BorrowedRecord> records = borrowedRecordRepository.findAll();
        return records.stream().map(this::toDTO).toList();
    }

    public List<BorrowedRecordDTO> getBorrowedRecordsByBorrower(Integer borrowerId) {
        return borrowedRecordRepository.findByBorrowerId(borrowerId).stream().map(this::toDTO).toList();
    }

    private BorrowedRecordDTO toDTO(BorrowedRecord records) {
        BorrowedRecordDTO dto = new BorrowedRecordDTO();
        dto.setBorrowedRecordId(records.getBorrowedRecordId());
        dto.setBorrowerId(records.getBorrowerId());
        dto.setBookRecordId(records.getBookRecordId());
        dto.setBorrowDate(records.getBorrowDate());
        dto.setDueDate(records.getDueDate());
        dto.setReturnDate(records.getReturnDate());
        return dto;
    }

    public BookRecordDTO toBookRecordDTO(BookRecord bookRecord) {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookRecordId(bookRecord.getBookRecordId());
        dto.setBookId(bookRecord.getBook().getId());
        dto.setIsbn(bookRecord.getIsbn());
        dto.setTitle(bookRecord.getTitle());
        dto.setAuthor(bookRecord.getAuthor());
        dto.setStatus(bookRecord.getStatus());
        dto.setBorrowerId(bookRecord.getBorrowerId());

        // Fetch borrow history
        List<BorrowedRecordDTO> borrowedRecords = borrowedRecordRepository
                .findByBookRecordId(bookRecord.getBookRecordId())
                .stream()
                .map(this::toDTO)
                .toList();

        dto.setBorrowedRecords(borrowedRecords);
        return dto;
    }

}
