package com.example.library.ServiceTest;

import com.example.library.dto.BorrowerDTO;
import com.example.library.model.Borrower;
import com.example.library.service.BorrowerService;
import com.example.library.repository.BorrowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BorrowerServiceTest {

    private BorrowerRepository borrowerRepository;
    private BorrowerService borrowerService;

    @BeforeEach
    void setUp() {
        borrowerRepository = Mockito.mock(BorrowerRepository.class);
        borrowerService = new BorrowerService(borrowerRepository);
    }

    @Test
    void testRegisterBorrower_Success() {
        BorrowerDTO dto = new BorrowerDTO();
        dto.setName("test");
        dto.setEmail("test@example.com");

        when(borrowerRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        Borrower saved = new Borrower();
        saved.setId(1);
        saved.setName(dto.getName());
        saved.setEmail(dto.getEmail());

        when(borrowerRepository.save(any(Borrower.class))).thenReturn(saved);

        BorrowerDTO result = borrowerService.registerBorrower(dto);

        assertEquals(1, result.getId());
        assertEquals("test", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testRegisterBorrower_DuplicateEmailThrowsException() {
        BorrowerDTO dto = new BorrowerDTO();
        dto.setName("test");
        dto.setEmail("test@example.com");

        when(borrowerRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> borrowerService.registerBorrower(dto));
    }

    @Test
    void testGetAllBorrowers() {
        Borrower borrower = new Borrower();
        borrower.setId(1);
        borrower.setName("test");
        borrower.setEmail("test@example.com");

        when(borrowerRepository.findAll()).thenReturn(List.of(borrower));

        List<BorrowerDTO> result = borrowerService.getAllBorrowers();

        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getName());
    }

    @Test
    void testUpdateBorrower_Success() {
        Borrower existing = new Borrower();
        existing.setId(1);
        existing.setName("Old Name");
        existing.setEmail("old@example.com");

        BorrowerDTO dto = new BorrowerDTO();
        dto.setName("New Name");
        dto.setEmail("new@example.com");

        when(borrowerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(borrowerRepository.existsByNameAndEmail(dto.getName(), dto.getEmail())).thenReturn(false);

        Borrower updated = new Borrower();
        updated.setId(1);
        updated.setName(dto.getName());
        updated.setEmail(dto.getEmail());

        when(borrowerRepository.save(existing)).thenReturn(updated);

        BorrowerDTO result = borrowerService.updateBorrower(1, dto);

        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void testUpdateBorrower_DuplicateThrowsException() {
        Borrower existing = new Borrower();
        existing.setId(1);
        existing.setName("Old Name");
        existing.setEmail("old@example.com");

        BorrowerDTO dto = new BorrowerDTO();
        dto.setName("New Name");
        dto.setEmail("new@example.com");

        when(borrowerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(borrowerRepository.existsByNameAndEmail(dto.getName(), dto.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> borrowerService.updateBorrower(1, dto));
    }

    @Test
    void testDeleteBorrower() {
        // Arrange
        when(borrowerRepository.existsById(1)).thenReturn(true);
        doNothing().when(borrowerRepository).deleteById(1);

        // Act
        borrowerService.deleteBorrower(1);

        // Assert
        verify(borrowerRepository).existsById(1);
        verify(borrowerRepository).deleteById(1);
    }

    @Test
    void testDeleteBorrower_notFoundThrowsException() {
        // Arrange
        when(borrowerRepository.existsById(999)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> borrowerService.deleteBorrower(999));

        assertEquals("Borrower not found", exception.getMessage());
        verify(borrowerRepository).existsById(999);
        verify(borrowerRepository, never()).deleteById(anyInt());
    }

}
