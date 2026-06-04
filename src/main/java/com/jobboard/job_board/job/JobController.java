package com.jobboard.job_board.job;

import com.jobboard.job_board.job.dto.CursorResponse;
import com.jobboard.job_board.job.dto.JobPageResponse;
import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<JobResponseDTO>> getJobsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    // delete job
    // RECRUITER only — only recruiters can delete job
    @PreAuthorize("hasRole('RECRUITER)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("deleted");
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
                                                        @RequestParam(defaultValue = "3") int
                                                                size) {
        return ResponseEntity.ok(jobService.getJobsCursor(cursor, size));
    }

}

