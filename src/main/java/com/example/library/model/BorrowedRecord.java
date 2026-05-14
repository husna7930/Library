package com.example.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "borrower_record")
public class BorrowedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowedRecordId;

    private Integer bookRecordId;

    private Integer borrowerId;

    private Integer bookId;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    private LocalDate dueDate;

    public Integer getBorrowedRecordId() {
        return borrowedRecordId;
    }

    public void setBorrowedRecordId(Integer borrowedRecordId) {
        this.borrowedRecordId = borrowedRecordId;
    }

    public Integer getBookRecordId() {
        return bookRecordId;
    }

    public void setBookRecordId(Integer bookRecordId) {
        this.bookRecordId = bookRecordId;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
