package com.jobboard.job_board.Application;

import com.jobboard.job_board.Application.dto.ApplicationResponseDto;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    // POST /api/applications?userId=1&jobId=2&resumeUrl=...
    @PreAuthorize("hasRole('APPLICANT')")
    @PostMapping
    public ResponseEntity <ApplicationResponseDto> apply(@RequestParam Long userId, @RequestParam Long jobId,
                                                         @RequestParam String resumeUrl){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationService.applyToJob(userId,jobId,resumeUrl));
    }

    // GET /api/applications/user/1
    // APPLICANT only — applicant views own applications
    @PreAuthorize("hasRole('APPLICANT')")
    @GetMapping("/user/{userId}")
    public ResponseEntity <List<ApplicationResponseDto>> getByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(applicationService.getApplicationsByUser(userId));

    }
    // GET /api/applications/job/1
    // applicants for a job
    // RECRUITER only — recruiter views applicants for their job
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity <List<ApplicationResponseDto>> getByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    // PATCH /api/applications/1/status?newStatus=SHORTLISTED
    // update status
    // RECRUITER only — recruiter shortlists/rejects
    @PreAuthorize("hasRole('RECRUITER")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus newStatus
    ) {
        return ResponseEntity.ok(applicationService.updateStatus(id, newStatus));
    }

    // withdraw or delete
    // APPLICANT only — applicant withdraws own application
    @PreAuthorize("hasRole('APPLICANT')")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable Long id) {
        applicationService.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    // Testing purpose
    @GetMapping("/all")
    public ResponseEntity<List<ApplicationResponseDto>> getAll(){
        return ResponseEntity.ok(applicationService.getHistory());
    }
}
