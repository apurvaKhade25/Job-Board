package com.jobboard.job_board.company;

import com.jobboard.job_board.company.Dto.CompanyRequest;
import com.jobboard.job_board.company.Dto.CompanyResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class CompanyService {
    private final CompanyRepo companyRepo;

    //create company
    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .email(request.getEmail())
                .website(request.getWebsite())
                .location(request.getLocation())
                .build();
        return toResponse(companyRepo.save(company));
    }

    //get history
    public CompanyResponse getHistory(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found with id" + id));
        return toResponse(company);

    }

    //get all
    public List<CompanyResponse> getEverything() {
        return companyRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //delete
    public void deleteCompany(Long id) {
        if (!companyRepo.existsById(id)) {
            throw new RuntimeException("Company not found with id: " + id);
        }
        companyRepo.deleteById(id);
    }


    private CompanyResponse toResponse(Company c) {
        CompanyResponse response = new CompanyResponse();
        response.setId(c.getId());
        response.setName(c.getName());
        response.setEmail(c.getName());
        response.setWebsite(c.getWebsite());
        response.setLocation(c.getLocation());
        return response;
    }
}
