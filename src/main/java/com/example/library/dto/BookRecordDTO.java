package com.example.library.dto;

import com.example.library.model.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BookRecordDTO {

    private Integer bookRecordId;

    @JsonIgnore
    private Book book;

    @NotNull(message = "Book ID cannot be null")
    private Integer bookId;

    @NotBlank(message = "ISBN cannot be null")
    private String isbn;

    @NotBlank(message = "Title cannot be null")
    private String title;

    @NotBlank(message = "Author cannot be null")
    private String author;

    private String status;

    private Integer borrowerId;

    private List<BorrowedRecordDTO> borrowedRecords;

    public Integer getBookRecordId() {
        return bookRecordId;
    }

    public void setBookRecordId(Integer bookRecordId) {
        this.bookRecordId = bookRecordId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public List<BorrowedRecordDTO> getBorrowedRecords() {
        return borrowedRecords;
    }

    public void setBorrowedRecords(List<BorrowedRecordDTO> borrowedRecords) {
        this.borrowedRecords = borrowedRecords;
    }

}
