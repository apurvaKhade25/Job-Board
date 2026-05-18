package com.jobboard.job_board.Application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    // POST /api/applications?userId=1&jobId=2&resumeUrl=...
    @PostMapping
    public ResponseEntity <Application> apply(@RequestParam Long userId,@RequestParam Long jobId,
                                              @RequestParam String resumeUrl){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationService.applyToJob(userId,jobId,resumeUrl));
    }

    // GET /api/applications/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity <List<Application>> getByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(applicationService.getApplicationsByUser(userId));

    }
    // GET /api/applications/job/1
    @GetMapping("/job/{jobId}")
    public ResponseEntity <List<Application>> getByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    // PATCH /api/applications/1/status?newStatus=SHORTLISTED
    @PatchMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus newStatus
    ) {
        return ResponseEntity.ok(applicationService.updateStatus(id, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable Long id) {
        applicationService.withdraw(id);
        return ResponseEntity.noContent().build();
    }
}
