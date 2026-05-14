package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.service.BookService;
import com.example.library.dto.BookRegistrationResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookRegistrationResponse> addBook(@Valid @RequestBody BookDTO bookDto) {
        BookRegistrationResponse response = bookService.registerBook(bookDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookRegistrationResponse> updateBook(@PathVariable Integer bookId,
            @Valid @RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(bookService.updateBook(bookId, bookDto));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book " + bookId + " is successfully deleted.");
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

}