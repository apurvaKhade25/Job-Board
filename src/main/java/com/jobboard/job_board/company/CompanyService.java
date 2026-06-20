package com.jobboard.job_board.company;

import com.jobboard.job_board.Exception.ResourceNotFoundException;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.company.Dto.CompanyRequest;
import com.jobboard.job_board.company.Dto.CompanyResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepo companyRepo;
    private final UserRepo userRepo;

    //create company
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
    @Transactional(readOnly = true )
    public CompanyResponse getHistory(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id" + id));
        return toResponse(company);

    }

    //get all
    @Transactional(readOnly = true)
    public List<CompanyResponse> getEverything() {
        return companyRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

//    delete
    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepo.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id: " + id);
        }
        List <Users> users = userRepo.findByCompanyId(id);
        if (!users.isEmpty()) {
            throw new IllegalStateException("Cannot delete company with associated users.");
        }
        companyRepo.deleteById(id);
    }


    public CompanyResponse toResponse(Company c) {
        CompanyResponse response = new CompanyResponse();
        response.setId(c.getId());
        response.setName(c.getName());
        response.setEmail(c.getEmail());
        response.setWebsite(c.getWebsite());
        response.setLocation(c.getLocation());
        return response;
    }
}
