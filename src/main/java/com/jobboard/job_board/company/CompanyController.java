package com.jobboard.job_board.company;

import com.jobboard.job_board.company.Dto.CompanyRequest;
import com.jobboard.job_board.company.Dto.CompanyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    //create company
    // RECRUITER only — recruiters create companies
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping()
    public ResponseEntity <CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest companyRequest){
        return ResponseEntity.status(201).body(companyService.createCompany(companyRequest));

    }

    //get by company
    //PUBLIC - anyone can view by id
    @GetMapping("/{id}")
    public ResponseEntity <CompanyResponse> getHistory(@PathVariable Long id){
        return ResponseEntity.status(201).body(companyService.getHistory(id));
    }

    //get all companies
    // PUBLIC - anyone can view
    @GetMapping()
    public ResponseEntity <List<CompanyResponse>> getEverything(){
        return ResponseEntity.ok((companyService.getEverything()));
    }

    //delete by id
    // RECRUITER only — recruiters deletes companies
    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity <String> delete(@PathVariable Long id){
        companyService.deleteCompany(id);
        return ResponseEntity.ok("deleted");
    }


}
