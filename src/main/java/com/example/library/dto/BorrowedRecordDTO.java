package com.example.library.dto;

import java.time.LocalDate;

public class BorrowedRecordDTO {

    private Integer borrowedRecordId;
    private Integer borrowerId;
    private Integer bookRecordId;
    private Integer bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Integer getBorrowedRecordId() {
        return borrowedRecordId;
    }

    public void setBorrowedRecordId(Integer borrowedRecordId) {
        this.borrowedRecordId = borrowedRecordId;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Integer getBookRecordId() {
        return bookRecordId;
    }

    public void setBookRecordId(Integer bookRecordId) {
        this.bookRecordId = bookRecordId;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

}
