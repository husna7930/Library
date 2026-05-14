package com.example.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_record")
public class BookRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_record_seq")
    @SequenceGenerator(name = "book_record_seq", sequenceName = "book_record_seq", allocationSize = 1)
    private Integer bookRecordId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private String isbn;

    private String title;

    private String author;

    private String status;

    private Integer borrowerId;

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

}
