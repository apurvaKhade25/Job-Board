package com.jobboard.job_board.job;

import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.dto.CursorResponse;
import com.jobboard.job_board.job.dto.JobPageResponse;
import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("/api/job")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job posting and search endpoints")
public class JobController {
    private final JobService jobService;

    // create job
    // RECRUITER only — only recruiters post jobs

    @Operation(summary = "Create job posting (Recruiter)",
            description = "RECRUITER only — post a new job")
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/add")
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO jobRequestDTO) {
        return ResponseEntity.ok(jobService.createJob(jobRequestDTO));
    }

    // RECRUITER only — view their own company's jobs
    @Operation(summary = "Recruiter view own company job (Recruiter)",
            description = "RECRUITER only - views their jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/my-jobs")
    public ResponseEntity<JobPageResponse> getMyJobs(
            @AuthenticationPrincipal Users currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(
                jobService.getMyJobs(currentUser.getEmail(), page, size, sortBy, sortDir)
        );
    }



    // get job by id
    //PUBLIC - Anyone can view job by id
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getHistoryByid(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getHistoryByid(id));
    }

    //get all jobs
    //PUBLIC - Anyone can view jobs
    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getHistory() {
        return ResponseEntity.ok(jobService.getHistory());
    }

    //get jobs by company
    //PUBLIC - Anyone can view job by company
    @GetMapping("/Company/{companyId}")
    public ResponseEntity<JobPageResponse> getJobsByCompany(@PathVariable Long companyId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "3") int size,
                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                 @RequestParam(defaultValue = "asc")
                                                                     String sortDir) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId,page, size, sortBy, sortDir));
    }

    // Update a job
    // RECRUITER only — only recruiters can uodate job
    @Operation(summary = "Update job status (Recruiter)",
            description = "Recruiter updates job status")
    @PreAuthorize("hasRole('RECRUITER')")
    @PatchMapping ("/jobstatus/{id}")
    public ResponseEntity<JobResponseDTO> updateJobByStatus(@PathVariable Long id,@RequestParam JobStatus jobStatus,
                                                            @AuthenticationPrincipal Users current_user) {
        String recruiterEmail=current_user.getEmail();
        System.out.println(current_user.getClass());
        return ResponseEntity.ok(jobService.updateJobStatus(id,jobStatus,recruiterEmail));
    }

    //    offset pagination
    //PUBLIC - Anyone can view jobs
    @Operation(summary = "Get all jobs",
            description = "Public — paginated job feed")
    @GetMapping("/all")
    public ResponseEntity<JobPageResponse> getAllJobs(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc")
                                                      String sortDir) {
        return ResponseEntity.ok(jobService.getAlljobsPaginated(page, size, sortBy, sortDir));
    }

    // PUBLIC — anyone can search
    @Operation(summary = "Search jobs by keyword")
    @GetMapping("/search")
    public ResponseEntity<JobPageResponse> search(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, page, size));
    }

    // PUBLIC — anyone can filter
    @Operation(summary = "Filter by location")
    @GetMapping("/location")
    public ResponseEntity<JobPageResponse> searchLocation(@RequestParam String location,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.filterByLocation(location, page, size));
    }

    // PUBLIC — cursor based feed
    @Operation(summary = "Infinite scroll job feed",
            description = "Cursor based pagination")
    @GetMapping("/cursor")
    public ResponseEntity<CursorResponse> getJobsCursor(@RequestParam(required = false) Long cursor,
                                                        @RequestParam(defaultValue = "1") int
                                                                size) {
        return ResponseEntity.ok(jobService.getJobsCursor(cursor, size));
    }

}

