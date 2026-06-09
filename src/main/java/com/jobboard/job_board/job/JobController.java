package com.jobboard.job_board.job;

import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.dto.CursorResponse;
import com.jobboard.job_board.job.dto.JobPageResponse;
import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
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
public class JobController {
    private final JobService jobService;

    // create job
    // RECRUITER only — only recruiters post jobs
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/add")
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO jobRequestDTO) {
        return ResponseEntity.ok(jobService.createJob(jobRequestDTO));
    }

    // RECRUITER only — view their own company's jobs
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
    @GetMapping("/all")
    public ResponseEntity<JobPageResponse> getAllJobs(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc")
                                                      String sortDir) {
        return ResponseEntity.ok(jobService.getAlljobsPaginated(page, size, sortBy, sortDir));
    }

    // PUBLIC — anyone can search
    @GetMapping("/search")
    public ResponseEntity<JobPageResponse> search(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, page, size));
    }

    // PUBLIC — anyone can filter
    @GetMapping("/location")
    public ResponseEntity<JobPageResponse> searchLocation(@RequestParam String location,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.filterByLocation(location, page, size));
    }

    // PUBLIC — cursor based feed
    @GetMapping("/cursor")
    public ResponseEntity<CursorResponse> getJobsCursor(@RequestParam(required = false) Long cursor,
                                                        @RequestParam(defaultValue = "1") int
                                                                size) {
        return ResponseEntity.ok(jobService.getJobsCursor(cursor, size));
    }

}

