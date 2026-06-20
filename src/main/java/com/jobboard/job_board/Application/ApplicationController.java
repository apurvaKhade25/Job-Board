package com.jobboard.job_board.Application;

import com.jobboard.job_board.Application.dto.ApplicationResponseDto;
import com.jobboard.job_board.Upload.FileUploadService;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Job application management")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final FileUploadService fileUploadService;

    // POST /api/applications?userId=1&jobId=2&resumeUrl=...
    @Operation(summary = "Apply to a job",
            description = "APPLICANT only — must have resume uploaded")
    @PreAuthorize("hasRole('APPLICANT')")
    @PostMapping("/add/job")
    public ResponseEntity<ApplicationResponseDto> apply(@RequestParam Long jobId,
                                                        @AuthenticationPrincipal Users current_user) {
//        String resumeUrl= fileUploadService.uploadResume();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationService.applyToJob(current_user.getId(), jobId));
    }

    // GET /api/applications/user/1
    // APPLICANT only — applicant views own applications
    @Operation(summary = "View my applications",
            description = "APPLICANT only")
    @PreAuthorize("hasRole('APPLICANT')")
    @GetMapping("/my-application")
    public ResponseEntity<List<ApplicationResponseDto>> getByUserId(@AuthenticationPrincipal Users current_user) {
        return ResponseEntity.ok(applicationService.getApplicationsByUser(current_user.getId()));

    }

    // GET /api/applications/job/1
    // applicants for a job
    // RECRUITER only — recruiter views applicants for their job
    @Operation(summary = "View applicants for a job",
            description = "RECRUITER only — own company jobs only")
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponseDto>> getByJobId(@PathVariable Long jobId,
                                                                   @AuthenticationPrincipal Users current_user) throws AccessDeniedException {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId, current_user.getEmail()));
    }

    // PATCH /api/applications/1/status?newStatus=SHORTLISTED
    // update status
    // RECRUITER only — recruiter shortlists/rejects
    @PreAuthorize("hasRole('RECRUITER')")
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

}
