package com.example.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.library.service.BookRecordService;
import com.example.library.dto.BookRecordDTO;
import java.util.List;

@RestController
@RequestMapping("/api/book_record")
public class BookRecordController {

    private final BookRecordService bookRecordService;

    public BookRecordController(BookRecordService bookRecordService) {
        this.bookRecordService = bookRecordService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<BookRecordDTO> registerCopy(@PathVariable Integer bookId) {
        BookRecordDTO saved = bookRecordService.registerCopy(bookId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<BookRecordDTO> getAllBookRecords() {
        return bookRecordService.getAllBookRecords();
    }

    @DeleteMapping("/{bookRecordId}")
    public ResponseEntity<String> deleteCopies(@PathVariable Integer bookRecordId) {
        bookRecordService.deleteCopies(bookRecordId);
        return ResponseEntity.ok("Book Copy " + bookRecordId + " is successfully deleted.");
    }

    @GetMapping("/isbn/{isbn}")
    public List<BookRecordDTO> getRecordsByIsbn(@PathVariable String isbn) {
        return bookRecordService.getRecordsByIsbn(isbn)
                .stream()
                .map(bookRecordService::toDTO)
                .toList();
    }

}
