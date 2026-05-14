package com.example.library.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class BookDTO {

    @Id
    @NotBlank(message = "ID cannot be null")
    private Integer id;

    @NotBlank(message = "ISBN cannot be null")
    private String isbn;

    @NotBlank(message = "Title cannot be null")
    private String title;

    @NotBlank(message = "Author cannot be null")
    private String author;

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

}
