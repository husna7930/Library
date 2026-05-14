package com.example.library.dto;

import java.util.List;

public class BookRegistrationResponse {

    private String message;
    private BookDTO book;
    private List<BookRecordDTO> bookRecord;

    public BookRegistrationResponse(String message, BookDTO book, List<BookRecordDTO> bookRecord) {
        this.message = message;
        this.book = book;
        this.bookRecord = bookRecord;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    public List<BookRecordDTO> getBookRecord() {
        return bookRecord;
    }

    public void setBookRecord(List<BookRecordDTO> bookRecord) {
        this.bookRecord = bookRecord;
    }

}
