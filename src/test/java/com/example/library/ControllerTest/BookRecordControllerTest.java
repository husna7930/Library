package com.example.library.ControllerTest;

import com.example.library.dto.BookRecordDTO;
import com.example.library.model.BookRecord;
import com.example.library.service.BookRecordService;
import com.example.library.controller.BookRecordController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRecordController.class)
class BookRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRecordService bookRecordService;

    @Test
    void testRegisterCopy() throws Exception {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookId(10);

        Mockito.when(bookRecordService.registerCopy(eq(10))).thenReturn(dto);

        mockMvc.perform(post("/api/book_record/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(10));
    }

    @Test
    void testGetAllBookRecords() throws Exception {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookRecordId(101);

        Mockito.when(bookRecordService.getAllBookRecords()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/book_record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookRecordId").value(101));
    }

    @Test
    void testDeleteCopies() throws Exception {
        Mockito.doNothing().when(bookRecordService).deleteCopies(101);

        mockMvc.perform(delete("/api/book_record/101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book Copy 101 is successfully deleted."));

        Mockito.verify(bookRecordService).deleteCopies(101);

    }

    @Test
    void testGetRecordsByIsbn() throws Exception {
        // Mock entity
        BookRecord record = new BookRecord();
        record.setBookRecordId(109);
        record.setIsbn("1111-1111-1111-1111");
        record.setTitle("Python");
        record.setAuthor("Alex S.Le");
        record.setStatus("AVAILABLE");

        // Mock DTO conversion
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookRecordId(109);
        dto.setBookId(1);
        dto.setIsbn("1111-1111-1111-1111");
        dto.setTitle("Python");
        dto.setAuthor("Alex S.Le");
        dto.setStatus("AVAILABLE");
        dto.setBorrowerId(null);

        // Service returns entity
        Mockito.when(bookRecordService.getRecordsByIsbn("1111-1111-1111-1111"))
                .thenReturn(List.of(record));
        // Service converts entity → DTO
        Mockito.when(bookRecordService.toDTO(record)).thenReturn(dto);

        mockMvc.perform(get("/api/book_record/isbn/1111-1111-1111-1111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("1111-1111-1111-1111"))
                .andExpect(jsonPath("$[0].title").value("Python"))
                .andExpect(jsonPath("$[0].author").value("Alex S.Le"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].bookRecordId").value(109));
    }

}
