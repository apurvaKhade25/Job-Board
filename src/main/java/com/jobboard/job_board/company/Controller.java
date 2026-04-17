package com.jobboard.job_board.company;

import com.jobboard.job_board.company.Dto.CompanyRequest;
import com.jobboard.job_board.company.Dto.CompanyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class Controller {

    private final CompanyService companyService;

    @PostMapping("/add")
    public ResponseEntity <CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest companyRequest){
        return ResponseEntity.status(201).body(companyService.createCompany(companyRequest));

    }

    @GetMapping("/history/{id}")
    public ResponseEntity <CompanyResponse> getHistory(@PathVariable Long id){
        return ResponseEntity.ok(companyService.getHistory(id));
    }

    @GetMapping("/evrything")
    public ResponseEntity <List<CompanyResponse>> getEverything(){
        return ResponseEntity.ok((companyService.getEverything()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity <String> delete(@PathVariable Long id){
        companyService.deleteCompany(id);
        return ResponseEntity.ok("deleted");
    }


}
