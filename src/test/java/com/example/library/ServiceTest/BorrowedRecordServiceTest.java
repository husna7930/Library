package com.example.library.ServiceTest;

import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BorrowedRecordDTO;
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
    void testRegisterBookRent_Success() {
        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookRecordId(1);
        bookRecord.setStatus("AVAILABLE");

        when(bookRecordRepository.findById(1)).thenReturn(Optional.of(bookRecord));

        // Mock save
        when(borrowedRecordRepository.save(any(BorrowedRecord.class)))
                .thenAnswer(invocation -> {
                    BorrowedRecord r = invocation.getArgument(0);
                    r.setBorrowedRecordId(100);
                    return r;
                });

        BookRecordDTO result = borrowedRecordService.registerBookRent(10, 1);

        assertEquals(1, result.getBookRecordId());
        assertEquals("BORROWED", result.getStatus());
        assertEquals(10, result.getBorrowerId());
        assertFalse(result.getBorrowedRecords().isEmpty());
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
    void testReturnBook_Success() {
        BorrowedRecord records = new BorrowedRecord();
        records.setBorrowedRecordId(100);
        records.setBorrowerId(10);
        records.setBookRecordId(1);
        records.setBorrowDate(LocalDate.now().minusDays(5));
        records.setDueDate(LocalDate.now().plusDays(9));

        BookRecord bookRecord = new BookRecord();
        bookRecord.setBookRecordId(1);
        bookRecord.setStatus("BORROWED");
        bookRecord.setBorrowerId(10);

        when(borrowedRecordRepository.findById(100)).thenReturn(Optional.of(records));
        when(bookRecordRepository.findById(1)).thenReturn(Optional.of(bookRecord));
        when(borrowedRecordRepository.save(records)).thenReturn(records);

        BookRecordDTO result = borrowedRecordService.returnBook(100);

        assertEquals("AVAILABLE", result.getStatus());
        assertNull(result.getBorrowerId());
        assertNotNull(result.getBorrowedRecords().get(0).getReturnDate());
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
}
