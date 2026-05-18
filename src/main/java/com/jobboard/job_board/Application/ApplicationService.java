package com.jobboard.job_board.Application;

import com.jobboard.job_board.Application.dto.ApplicationResponseDto;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.Job;
import com.jobboard.job_board.job.JobRepo;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Transactional
public class ApplicationService {
    private final ApplicationRepo applicationRepo;
    private final UserRepo usersRepo;
    private final JobRepo jobRepo;

    @Transactional
    public ApplicationResponseDto applyToJob(Long userId, Long jobId, String resumeUrl){
        // check user exists
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // check job exists
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        // check already applied — uses your existsByUserIdAndJobId query
        boolean alreadyApplied = applicationRepo.existsByUsersIdAndJobId(userId, jobId);
        if (alreadyApplied) {
            throw new RuntimeException("User already applied to this job");
        }

        // build and save
        Application application = Application.builder()
                .users(user)
                .job(job)
                .resumeUrl(resumeUrl)
                .status(ApplicationStatus.PENDING)  // default
                .build();

        return mapToDto(applicationRepo.save(application));
    }

    // 2. Get all applications by a user
    public List<ApplicationResponseDto> getApplicationsByUser(Long userId) {
        return applicationRepo.findByUsersId(userId).stream().map(this::mapToDto).toList();
    }

    // 3. Get all applications for a job (recruiter view)
    public List<ApplicationResponseDto> getApplicationsByJob(Long jobId) {
        return applicationRepo.findByJobId(jobId).stream().map(this::mapToDto).toList();
    }

    // 4. Update application status (recruiter shortlists/rejects)
    @Transactional
    public ApplicationResponseDto updateStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));

        application.setStatus(newStatus);
        return mapToDto(applicationRepo.save(application));  // @Transactional makes this optional
        // but explicit is clearer for now
    }

    // 5. Withdraw application
    @Transactional
    public void withdraw(Long applicationId) {
        if (!applicationRepo.existsById(applicationId)) {
            throw new RuntimeException("Application not found: " + applicationId);
        }
        applicationRepo.deleteById(applicationId);
    }

    //dto mapper
    private ApplicationResponseDto mapToDto(Application app){
        return ApplicationResponseDto.builder()
                .id(app.getId())
                .status(app.getStatus().name())
                .resumeUrl(app.getResumeUrl())
                .appliedAt(app.getApplied_at())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .userId(app.getUsers().getId())
                .userName(app.getUsers().getFullname())
                .build();
    }


}
