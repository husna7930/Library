package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.library.dto.BorrowerDTO;
import com.example.library.service.BorrowerService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public BorrowerDTO registerBorrower(@Valid @RequestBody BorrowerDTO borrowerDTO) {
        return borrowerService.registerBorrower(borrowerDTO);
    }

    @GetMapping
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    @PutMapping("/{id}")
    public BorrowerDTO updateBorrower(@PathVariable Integer id, @Valid @RequestBody BorrowerDTO borrowerDTO) {
        return borrowerService.updateBorrower(id, borrowerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorrower(@PathVariable Integer id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.ok("Borrower with id: " + id + " is successfully deleted.");
    }

}
