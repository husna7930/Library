package com.example.library.ServiceTest;

import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BorrowedRecordDTO;
import com.example.library.model.Book;
import com.example.library.model.BookRecord;
import com.example.library.model.BorrowedRecord;
import com.example.library.service.BorrowedRecordService;
import com.example.library.repository.BookRecordRepository;
import com.example.library.repository.BorrowedRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BorrowedRecordServiceTest {

    private BorrowedRecordRepository borrowedRecordRepository;
    private BookRecordRepository bookRecordRepository;
    private BorrowedRecordService borrowedRecordService;

    @BeforeEach
    void setUp() {
        borrowedRecordRepository = Mockito.mock(BorrowedRecordRepository.class);
        bookRecordRepository = Mockito.mock(BookRecordRepository.class);
        borrowedRecordService = new BorrowedRecordService(borrowedRecordRepository, bookRecordRepository);
    }

    @Test
    void rtestRegisterBookRent_success() {
        // Arrange
        Book book = new Book();
        book.setId(1);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookRecordId(100);
        bookRecord.setBook(book);
        bookRecord.setStatus("AVAILABLE");

        when(bookRecordRepository.findById(100)).thenReturn(Optional.of(bookRecord));
        when(borrowedRecordRepository.existsByBorrowerIdAndBookId(1, 1)).thenReturn(false);

        // Act
        BookRecordDTO dto = borrowedRecordService.registerBookRent(1, 100);

        // Assert
        assertEquals("BORROWED", dto.getStatus());
        assertEquals(1, dto.getBorrowerId());

        // Verify save calls
        verify(bookRecordRepository).save(bookRecord);
        verify(borrowedRecordRepository).save(any(BorrowedRecord.class));
    }

    @Test
    void testRegisterBookRent_BookAlreadyBorrowedThrowsException() {
        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookRecordId(1);
        bookRecord.setStatus("BORROWED");

        when(bookRecordRepository.findById(1)).thenReturn(Optional.of(bookRecord));

        assertThrows(ResponseStatusException.class,
                () -> borrowedRecordService.registerBookRent(10, 1));
    }

    @Test
    void testRegisterBookRent_borrowSameBook() {
        Book book = new Book();
        book.setId(1);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookRecordId(100);
        bookRecord.setBook(book);
        bookRecord.setStatus("AVAILABLE");

        when(bookRecordRepository.findById(100)).thenReturn(Optional.of(bookRecord));
        when(borrowedRecordRepository.existsByBorrowerIdAndBookId(1, 1)).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> borrowedRecordService.registerBookRent(1, 100));
    }

    @Test
    void testReturnBook_success() {
        BorrowedRecord borrowedRecord = new BorrowedRecord();
        borrowedRecord.setBorrowedRecordId(200);
        borrowedRecord.setBookRecordId(100);

        Book book = new Book();
        book.setId(1);

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookRecordId(100);
        bookRecord.setBook(book);
        bookRecord.setStatus("BORROWED");
        bookRecord.setBorrowerId(1);

        when(borrowedRecordRepository.findById(200)).thenReturn(Optional.of(borrowedRecord));
        when(bookRecordRepository.findById(100)).thenReturn(Optional.of(bookRecord));

        BookRecordDTO dto = borrowedRecordService.returnBook(200);

        assertEquals("AVAILABLE", dto.getStatus());
        assertNull(dto.getBorrowerId());

        verify(bookRecordRepository).save(bookRecord);
        verify(borrowedRecordRepository).save(borrowedRecord);
    }

    @Test
    void testGetAllBorrowedRecords() {
        BorrowedRecord records = new BorrowedRecord();
        records.setBorrowedRecordId(100);
        records.setBorrowerId(10);
        records.setBookRecordId(1);
        records.setBorrowDate(LocalDate.now());
        records.setDueDate(LocalDate.now().plusDays(14));

        when(borrowedRecordRepository.findAll()).thenReturn(List.of(records));

        List<BorrowedRecordDTO> result = borrowedRecordService.getAllBorrowedRecords();

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getBorrowerId());
    }

    @Test
    void testRegisterBookRent_bookNotFound() {
        when(bookRecordRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> borrowedRecordService.registerBookRent(1, 999));
    }
}
