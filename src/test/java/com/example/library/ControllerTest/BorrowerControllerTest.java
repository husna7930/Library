package com.example.library.ControllerTest;

import com.example.library.dto.BorrowerDTO;
import com.example.library.service.BorrowerService;
import com.example.library.controller.BorrowerController;
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

@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowerService borrowerService;

    @Test
    void testRegisterBorrower() throws Exception {
        BorrowerDTO dto = new BorrowerDTO();
        dto.setId(1);
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        Mockito.when(borrowerService.registerBorrower(any(BorrowerDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/borrowers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test User\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetAllBorrowers() throws Exception {
        BorrowerDTO dto = new BorrowerDTO();
        dto.setId(1);
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        Mockito.when(borrowerService.getAllBorrowers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/borrowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void testUpdateBorrower() throws Exception {
        BorrowerDTO updated = new BorrowerDTO();
        updated.setId(1);
        updated.setName("Updated Name");
        updated.setEmail("updated@example.com");

        Mockito.when(borrowerService.updateBorrower(eq(1), any(BorrowerDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/borrowers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteBorrower() throws Exception {
        mockMvc.perform(delete("/api/borrowers/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Borrower with id: 1 is successfully deleted."));

        Mockito.verify(borrowerService).deleteBorrower(1);
    }
}
