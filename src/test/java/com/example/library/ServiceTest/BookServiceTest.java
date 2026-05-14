package com.example.library.ServiceTest;

import com.example.library.dto.BookDTO;
import com.example.library.dto.BookRegistrationResponse;
import com.example.library.model.Book;
import com.example.library.model.BookRecord;
import com.example.library.repository.BookRecordRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookRecordRepository bookRecordRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookRecordRepository = Mockito.mock(BookRecordRepository.class);
        bookService = new BookService(bookRepository, bookRecordRepository);
    }

    @Test
    void testRegisterBook_Success() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setIsbn("1111-1111-2222");

        when(bookRepository.existsByIsbn(dto.getIsbn()))
                .thenReturn(false);

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setTitle(dto.getTitle());
        savedBook.setAuthor(dto.getAuthor());
        savedBook.setIsbn(dto.getIsbn());

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookRecord savedRecord = new BookRecord();
        savedRecord.setBook(savedBook);
        savedRecord.setIsbn(savedBook.getIsbn());
        savedRecord.setTitle(savedBook.getTitle());
        savedRecord.setAuthor(savedBook.getAuthor());
        savedRecord.setStatus("AVAILABLE");

        when(bookRecordRepository.save(any(BookRecord.class))).thenReturn(savedRecord);

        BookRegistrationResponse result = bookService.registerBook(dto);

        assertEquals("Effective Java", result.getBook().getTitle());
        assertEquals("Joshua Bloch", result.getBook().getAuthor());
        assertEquals("1111-1111-2222", result.getBook().getIsbn());
    }

    @Test
    void testRegisterBook_DuplicateThrowsException() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setIsbn("1111-1111-2222");

        when(bookRepository.existsByIsbn(dto.getIsbn()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> bookService.registerBook(dto));
    }

    @Test
    void testUpdateBook_Success() {
        BookDTO dto = new BookDTO();
        dto.setIsbn("54321");
        dto.setTitle("Updated Title");
        dto.setAuthor("Updated Author");

        Book existing = new Book();
        existing.setId(1);
        existing.setIsbn("12345");
        existing.setTitle("Old Title");
        existing.setAuthor("Old Author");

        BookRecord recordsBookRecord = new BookRecord();
        recordsBookRecord.setBookRecordId(100);
        recordsBookRecord.setBook(existing);
        recordsBookRecord.setIsbn("12345");
        recordsBookRecord.setTitle("Old Title");
        recordsBookRecord.setAuthor("Old Author");
        recordsBookRecord.setStatus("AVAILABLE");

        when(bookRepository.findById(1)).thenReturn(Optional.of(existing));
        when(bookRepository.existsByIsbn("54321")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(existing);
        when(bookRecordRepository.findByBookId(1)).thenReturn(List.of(recordsBookRecord));
        when(bookRecordRepository.save(any(BookRecord.class))).thenReturn(recordsBookRecord);

        BookRegistrationResponse response = bookService.updateBook(1, dto);

        assertEquals("Book updated successfully", response.getMessage());
        assertEquals("Updated Title", response.getBook().getTitle());
        assertEquals("54321", response.getBook().getIsbn());
        assertEquals(1, response.getBookRecord().size());
        assertEquals("Updated Title", response.getBookRecord().get(0).getTitle());
    }

    @Test
    void testUpdateBook_NotFoundThrowsException() {
        BookDTO dto = new BookDTO();
        dto.setTitle("New Title");
        dto.setAuthor("New Author");
        dto.setIsbn("1111");

        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> bookService.updateBook(1, dto));
    }

    @Test
    void testDeleteBook_success() {

        when(bookRepository.existsById(1)).thenReturn(true);
        doNothing().when(bookRecordRepository).deleteByBookId(1);
        doNothing().when(bookRepository).deleteById(1);
        bookService.deleteBook(1);
        verify(bookRepository).existsById(1);
        verify(bookRecordRepository).deleteByBookId(1);
        verify(bookRepository).deleteById(1);

    }

    @Test
    void testGetAllBooks() {
        Book book = new Book();
        book.setId(1);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("1111-1111-2222");

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
    }

    @Test
    void testDeleteBook_nullId() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.deleteBook(null));
    }
}
