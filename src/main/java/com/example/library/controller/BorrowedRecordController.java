package com.example.library.controller;

import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BorrowedRecordDTO;
import com.example.library.service.BorrowedRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/borrowedRecords")
public class BorrowedRecordController {

    private final BorrowedRecordService borrowedRecordService;

    public BorrowedRecordController(BorrowedRecordService borrowedRecordService) {
        this.borrowedRecordService = borrowedRecordService;
    }

    @PostMapping("/borrow/{borrowerId}/{bookRecordId}")
    public ResponseEntity<BookRecordDTO> registerBookRent(
            @PathVariable Integer borrowerId,
            @PathVariable Integer bookRecordId) {
        return ResponseEntity.ok(borrowedRecordService.registerBookRent(borrowerId, bookRecordId));
    }

    @PostMapping("/return/{borrowedRecordId}")
    public ResponseEntity<BookRecordDTO> returnBook(@PathVariable Integer borrowedRecordId) {
        return ResponseEntity.ok(borrowedRecordService.returnBook(borrowedRecordId));
    }

    @GetMapping
    public ResponseEntity<List<BorrowedRecordDTO>> getAllBorrowedRecords() {
        return ResponseEntity.ok(borrowedRecordService.getAllBorrowedRecords());
    }

    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<BorrowedRecordDTO>> getBorrowedRecordsByBorrower(@PathVariable Integer borrowerId) {
        return ResponseEntity.ok(borrowedRecordService.getBorrowedRecordsByBorrower(borrowerId));
    }

}
