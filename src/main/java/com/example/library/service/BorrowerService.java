package com.example.library.service;

import com.example.library.model.Borrower;
import com.example.library.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import com.example.library.dto.BorrowerDTO;

import java.util.List;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public BorrowerDTO registerBorrower(BorrowerDTO dto) {

        if (borrowerRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        Borrower borrower = new Borrower();
        borrower.setName(dto.getName());
        borrower.setEmail(dto.getEmail());
        Borrower saved = borrowerRepository.save(borrower);
        return toResponseDTO(saved);
    }

    private BorrowerDTO toResponseDTO(Borrower borrower) {
        BorrowerDTO response = new BorrowerDTO();
        response.setId(borrower.getId());
        response.setName(borrower.getName());
        response.setEmail(borrower.getEmail());
        return response;
    }

    public List<BorrowerDTO> getAllBorrowers() {
        List<Borrower> borrowers = borrowerRepository.findAll();
        return borrowers.stream().map(this::toResponseDTO).toList();
    }

    public BorrowerDTO updateBorrower(Integer id, BorrowerDTO dto) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));

        if (borrowerRepository.existsByNameAndEmail(dto.getName(), dto.getEmail())) {
            throw new IllegalArgumentException("Borrower with this name and email already exists");
        }
        borrower.setName(dto.getName());
        borrower.setEmail(dto.getEmail());
        Borrower updated = borrowerRepository.save(borrower);
        return toResponseDTO(updated);
    }

    public void deleteBorrower(Integer id) {
        if (!borrowerRepository.existsById(id)) {
            throw new IllegalArgumentException("Borrower not found");
        }
        borrowerRepository.deleteById(id);
    }

}
