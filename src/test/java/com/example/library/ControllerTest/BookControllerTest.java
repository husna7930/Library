package com.example.library.ControllerTest;

import com.example.library.dto.BookDTO;
import com.example.library.dto.BookRegistrationResponse;
import com.example.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.library.controller.BookController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private BookService bookService;

        @Test
        void testAddBook() throws Exception {
                BookDTO book = new BookDTO();
                book.setTitle("Effective Java");
                book.setAuthor("Joshua Bloch");
                book.setIsbn("1111-1111-2222");

                BookRegistrationResponse response = new BookRegistrationResponse(
                                "Book registered successfully",
                                book,
                                null // or a BookRecordDTO if you want to include it
                );

                Mockito.when(bookService.registerBook(any(BookDTO.class))).thenReturn(response);

                mockMvc.perform(post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"isbn\":\"1111-1111-2222\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Book registered successfully"))
                                .andExpect(jsonPath("$.book.title").value("Effective Java"))
                                .andExpect(jsonPath("$.book.author").value("Joshua Bloch"))
                                .andExpect(jsonPath("$.book.isbn").value("1111-1111-2222"));
        }

        @Test
        void testUpdateBook() throws Exception {
                BookDTO updated = new BookDTO();
                updated.setId(1);
                updated.setTitle("Effective Java - 3rd Edition");
                updated.setAuthor("Joshua Bloch");
                updated.setIsbn("1111-1111-2222");

                BookRegistrationResponse response = new BookRegistrationResponse(
                                "Book updated successfully",
                                updated,
                                null // or a BookRecordDTO if you want to include it
                );

                Mockito.when(bookService.updateBook(eq(1), any(BookDTO.class))).thenReturn(response);

                mockMvc.perform(put("/api/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{\"title\":\"Effective Java - 3rd Edition\",\"author\":\"Joshua Bloch\",\"isbn\":\"1111-1111-2222\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Effective Java - 3rd Edition"));
        }

        @Test
        void testDeleteBook() throws Exception {
                mockMvc.perform(delete("/api/books/1"))
                                .andExpect(status().isNoContent());
                Mockito.verify(bookService).deleteBook(1);
        }

        @Test
        void testGetAllBooks() throws Exception {
                BookDTO book = new BookDTO();
                book.setId(1);
                book.setTitle("Effective Java");
                book.setAuthor("Joshua Bloch");
                book.setIsbn("1111-1111-2222");

                Mockito.when(bookService.getAllBooks()).thenReturn(List.of(book));

                mockMvc.perform(get("/api/books"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                                .andExpect(jsonPath("$[0].author").value("Joshua Bloch"));
        }
}
