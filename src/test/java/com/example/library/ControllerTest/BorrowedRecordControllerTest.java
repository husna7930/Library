package com.example.library.ControllerTest;

import com.example.library.dto.BookRecordDTO;
import com.example.library.dto.BorrowedRecordDTO;
import com.example.library.service.BorrowedRecordService;
import com.example.library.controller.BorrowedRecordController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowedRecordController.class)
class BorrowedRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowedRecordService borrowedRecordService;

    @Test
    void testRegisterBookRent_success() throws Exception {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookRecordId(109);
        dto.setBookId(1);
        dto.setIsbn("1234-1234-1234");
        dto.setTitle("Python");
        dto.setAuthor("Alex S.Le");
        dto.setStatus("BORROWED");

        Mockito.when(borrowedRecordService.registerBookRent(anyInt(), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/borrowedRecords/borrow/1/109"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRecordId").value(109))
                .andExpect(jsonPath("$.status").value("BORROWED"))
                .andExpect(jsonPath("$.title").value("Python"));
    }

    @Test
    void testReturnBook_success() throws Exception {
        BookRecordDTO dto = new BookRecordDTO();
        dto.setBookRecordId(109);
        dto.setBookId(1);
        dto.setIsbn("1234-1234-1234");
        dto.setTitle("Python");
        dto.setAuthor("Alex S.Le");
        dto.setStatus("AVAILABLE");

        Mockito.when(borrowedRecordService.returnBook(anyInt()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/borrowedRecords/return/109"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookRecordId").value(109))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void testGetAllBorrowedRecords_success() throws Exception {
        BorrowedRecordDTO record = new BorrowedRecordDTO();
        record.setBorrowedRecordId(200);
        record.setBorrowerId(1);
        record.setBookRecordId(109);

        Mockito.when(borrowedRecordService.getAllBorrowedRecords())
                .thenReturn(List.of(record));

        mockMvc.perform(get("/api/borrowedRecords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].borrowedRecordId").value(200))
                .andExpect(jsonPath("$[0].borrowerId").value(1))
                .andExpect(jsonPath("$[0].bookRecordId").value(109));
    }

    @Test
    void testGetBorrowedRecordsByBorrower_success() throws Exception {
        BorrowedRecordDTO record = new BorrowedRecordDTO();
        record.setBorrowedRecordId(201);
        record.setBorrowerId(2);
        record.setBookRecordId(110);

        Mockito.when(borrowedRecordService.getBorrowedRecordsByBorrower(2))
                .thenReturn(List.of(record));

        mockMvc.perform(get("/api/borrowedRecords/borrower/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].borrowedRecordId").value(201))
                .andExpect(jsonPath("$[0].borrowerId").value(2))
                .andExpect(jsonPath("$[0].bookRecordId").value(110));
    }
}
