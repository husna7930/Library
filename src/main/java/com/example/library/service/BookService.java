package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BookRegistrationResponse;
import com.example.library.model.Book;
import com.example.library.model.BookRecord;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BookRecordRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookRecordRepository bookRecordRepository;

    public BookService(BookRepository bookRepository, BookRecordRepository bookRecordRepository) {
        this.bookRepository = bookRepository;
        this.bookRecordRepository = bookRecordRepository;
    }

    public BookRegistrationResponse registerBook(BookDTO dto) {

        Book book = toEntity(dto);

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book is already exists");
        }

        Book saved = bookRepository.save(book);

        // When new book registered, create a BookRecord for it with status "AVAILABLE".
        BookRecord records = new BookRecord();
        records.setBook(saved);
        records.setIsbn(saved.getIsbn());
        records.setTitle(saved.getTitle());
        records.setAuthor(saved.getAuthor());
        records.setStatus("AVAILABLE");
        BookRecord savedRecord = bookRecordRepository.save(records);

        // response
        return new BookRegistrationResponse(
                "Book registered successfully",
                toDTO(saved),
                List.of(toRecordDTO(savedRecord)));
    }

    public BookRegistrationResponse updateBook(Integer bookId, BookDTO bookDTO) {

        if (bookId == null || bookDTO == null) {
            throw new IllegalArgumentException("Book ID and details must not be null");
        }

        // Update the Book itself
        Book existing = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        if (bookRepository.existsByIsbn(bookDTO.getIsbn()) &&
                !existing.getIsbn().equals(bookDTO.getIsbn())) {
            throw new IllegalArgumentException("Another book with this ISBN already exists");
        }

        existing.setTitle(bookDTO.getTitle());
        existing.setAuthor(bookDTO.getAuthor());
        existing.setIsbn(bookDTO.getIsbn());

        Book savedBook = bookRepository.save(existing);

        // Update all BookRecords linked to this Book
        List<BookRecordDTO> recordDTOs = new ArrayList<>();
        List<BookRecord> records = bookRecordRepository.findByBookId(bookId);
        for (BookRecord oldRecord : records) {
            oldRecord.setTitle(bookDTO.getTitle());
            oldRecord.setAuthor(bookDTO.getAuthor());
            oldRecord.setIsbn(bookDTO.getIsbn());
            BookRecord updatedRecord = bookRecordRepository.save(oldRecord);
            recordDTOs.add(toRecordDTO(updatedRecord));
        }

        return new BookRegistrationResponse(
                "Book updated successfully",
                toDTO(savedBook),
                recordDTOs);
    }

    @Transactional
    public void deleteBook(Integer bookId) {

        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }

        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist");
        }

        // First delete all BookRecords linked to this Book
        bookRecordRepository.deleteByBookId(bookId);

        // Then delete the Book itself
        bookRepository.deleteById(bookId);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        return dto;
    }

    public Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        return book;
    }

    private BookRecordDTO toRecordDTO(BookRecord records) {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookId(records.getBook().getId());
        dto.setBookRecordId(records.getBookRecordId());
        dto.setIsbn(records.getIsbn());
        dto.setTitle(records.getTitle());
        dto.setAuthor(records.getAuthor());
        dto.setStatus(records.getStatus());
        return dto;
    }

}
