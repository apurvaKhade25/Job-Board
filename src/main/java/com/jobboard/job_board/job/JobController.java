package com.jobboard.job_board.job;

import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    // create job
    @PostMapping("/add")
    public ResponseEntity <JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO jobRequestDTO){
        return ResponseEntity.ok(jobService.createJob(jobRequestDTO));
    }

    // get job by id
    @GetMapping("/{id}")
    public ResponseEntity <JobResponseDTO> getHistoryByid(@PathVariable Long id){
        return ResponseEntity.ok(jobService.getHistoryByid(id));
    }
    //get all jobs
    @GetMapping
    public ResponseEntity <List<JobResponseDTO>> getHistory(){
        return ResponseEntity.ok(jobService.getHistory());
    }

    //grt jobs by company
    @GetMapping("/Company/{companyId}")
    public ResponseEntity <List<JobResponseDTO>> getJobsByCompany(@PathVariable Long companyId){
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    // delete job
    @DeleteMapping("/delete/{id}")
    public ResponseEntity <String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("deleted");
    }
}

