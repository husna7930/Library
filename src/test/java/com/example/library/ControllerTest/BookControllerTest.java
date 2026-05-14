package com.example.library.ControllerTest;

import com.example.library.dto.BookDTO;
import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BookRegistrationResponse;
import com.example.library.controller.BookController;
import com.example.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        void testAddBook_success() throws Exception {
                BookDTO book = new BookDTO();
                book.setId(1);
                book.setTitle("Effective Java");
                book.setAuthor("Joshua Bloch");
                book.setIsbn("1111-1111-2222");

                BookRecordDTO record = new BookRecordDTO();
                record.setBookRecordId(109);
                record.setBookId(1);
                record.setIsbn("1111-1111-2222");
                record.setTitle("Effective Java");
                record.setAuthor("Joshua Bloch");
                record.setStatus("AVAILABLE");
                record.setBorrowerId(null);

                BookRegistrationResponse response = new BookRegistrationResponse(
                                "Book registered successfully",
                                book,
                                List.of(record));

                Mockito.when(bookService.registerBook(any(BookDTO.class))).thenReturn(response);

                mockMvc.perform(post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"isbn\":\"1111-1111-2222\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Book registered successfully"))
                                .andExpect(jsonPath("$.book.id").value(1))
                                .andExpect(jsonPath("$.book.title").value("Effective Java"))
                                .andExpect(jsonPath("$.book.author").value("Joshua Bloch"))
                                .andExpect(jsonPath("$.book.isbn").value("1111-1111-2222"))
                                .andExpect(jsonPath("$.bookRecord[0].bookRecordId").value(109))
                                .andExpect(jsonPath("$.bookRecord[0].status").value("AVAILABLE"));
        }

        @Test
        void testUpdateBook_success() throws Exception {
                BookDTO updated = new BookDTO();
                updated.setId(1);
                updated.setTitle("Effective Java - 3rd Edition");
                updated.setAuthor("Joshua Bloch");
                updated.setIsbn("1111-1111-2222");

                BookRegistrationResponse response = new BookRegistrationResponse(
                                "Book updated successfully",
                                updated,
                                List.of());

                Mockito.when(bookService.updateBook(eq(1), any(BookDTO.class))).thenReturn(response);

                mockMvc.perform(put("/api/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"Effective Java - 3rd Edition\",\"author\":\"Joshua Bloch\",\"isbn\":\"1111-1111-2222\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Book updated successfully"))
                                .andExpect(jsonPath("$.book.title").value("Effective Java - 3rd Edition"));
        }

        @Test
        void testDeleteBook_success() throws Exception {
                Mockito.doNothing().when(bookService).deleteBook(1);

                mockMvc.perform(delete("/api/books/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Book 1 is successfully deleted."));

                Mockito.verify(bookService).deleteBook(1);
        }

        @Test
        void testGetAllBooks_success() throws Exception {
                BookDTO book = new BookDTO();
                book.setId(1);
                book.setTitle("Effective Java");
                book.setAuthor("Joshua Bloch");
                book.setIsbn("1111-1111-2222");

                Mockito.when(bookService.getAllBooks()).thenReturn(List.of(book));

                mockMvc.perform(get("/api/books"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                                .andExpect(jsonPath("$[0].author").value("Joshua Bloch"))
                                .andExpect(jsonPath("$[0].isbn").value("1111-1111-2222"));
        }
}
