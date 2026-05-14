package com.example.library.ServiceTest;

import com.example.library.dto.BookRecordDTO;
import com.example.library.model.Book;
import com.example.library.model.BookRecord;
import com.example.library.service.BookRecordService;
import com.example.library.repository.BookRecordRepository;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookRecordServiceTest {

    private BookRecordRepository bookRecordRepository;
    private BookRepository bookRepository;
    private BookRecordService bookRecordService;

    @BeforeEach
    void setUp() {
        bookRecordRepository = Mockito.mock(BookRecordRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        bookRecordService = new BookRecordService(bookRecordRepository, bookRepository);
    }

    @Test
    void testRegisterCopy_Success() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("1111-1111-2222");
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        BookRecord savedRecord = new BookRecord();
        savedRecord.setBookRecordId(100);
        savedRecord.setBook(book);
        savedRecord.setIsbn(book.getIsbn());
        savedRecord.setTitle(book.getTitle());
        savedRecord.setAuthor(book.getAuthor());
        savedRecord.setStatus("AVAILABLE");

        when(bookRecordRepository.save(any(BookRecord.class))).thenReturn(savedRecord);

        BookRecordDTO result = bookRecordService.registerCopy(1);

        assertEquals(100, result.getBookRecordId());
        assertEquals("Effective Java", result.getTitle());
        assertEquals("Joshua Bloch", result.getAuthor());
        assertEquals("AVAILABLE", result.getStatus());
        assertEquals(1, result.getBookId());
    }

    @Test
    void testRegisterCopy_BookNotFoundThrowsException() {
        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> bookRecordService.registerCopy(999));
    }

    @Test
    void testGetRecordsByIsbn() {
        BookRecord records = new BookRecord();
        records.setBookRecordId(100);
        records.setIsbn("1111-1111-2222");
        records.setTitle("Effective Java");
        records.setAuthor("Joshua Bloch");
        records.setStatus("AVAILABLE");

        when(bookRecordRepository.findByIsbn("1111-1111-2222"))
                .thenReturn(List.of(records));

        List<BookRecord> result = bookRecordService.getRecordsByIsbn("1111-1111-2222");

        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
    }

    @Test
    void testGetAllBookRecords() {
        Book book = new Book();
        book.setId(1);

        BookRecord records = new BookRecord();
        records.setBookRecordId(100);
        records.setBook(book);
        records.setIsbn("1111-1111-2222");
        records.setTitle("Effective Java");
        records.setAuthor("Joshua Bloch");
        records.setStatus("AVAILABLE");

        when(bookRecordRepository.findAll()).thenReturn(List.of(records));

        List<BookRecordDTO> result = bookRecordService.getAllBookRecords();

        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
        assertEquals("AVAILABLE", result.get(0).getStatus());
    }

    @Test
    void testDeleteCopies_success() {

        when(bookRecordRepository.existsById(100)).thenReturn(true);
        doNothing().when(bookRecordRepository).deleteById(100);
        bookRecordService.deleteCopies(100);
        verify(bookRecordRepository).deleteById(100);
        verify(bookRecordRepository).existsById(100);

    }
}
