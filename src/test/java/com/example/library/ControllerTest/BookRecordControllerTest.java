package com.example.library.ControllerTest;

import com.example.library.dto.BookRecordDTO;
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
}
