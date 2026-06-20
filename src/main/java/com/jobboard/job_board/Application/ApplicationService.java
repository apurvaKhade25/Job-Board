package com.jobboard.job_board.Application;

import com.jobboard.job_board.Application.dto.ApplicationResponseDto;
import com.jobboard.job_board.Exception.BadRequestException;
import com.jobboard.job_board.Exception.DuplicateApplicationException;
import com.jobboard.job_board.Exception.ResourceNotFoundException;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.job.Job;
import com.jobboard.job_board.job.JobRepo;
import com.jobboard.job_board.job.JobStatus;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private final ApplicationRepo applicationRepo;
    private final UserRepo usersRepo;
    private final JobRepo jobRepo;

    // WRITE — covered by class level @Transactional
    // if any step fails → entire thing rolls back
    // user check fails → nothing saved
    // job check fails → nothing saved
    // duplicate check fails → nothing saved
    public ApplicationResponseDto applyToJob(Long userId, Long jobId) {
        // check user exists
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // check job exists
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));

        if(job.getJobStatus() !=JobStatus.OPEN){
            throw new BadRequestException("Application closed! Cannot be applied");
        }


        // check already applied — uses your existsByUserIdAndJobId query
        boolean alreadyApplied = applicationRepo.existsByUsersIdAndJobId(userId, jobId);
        if (alreadyApplied) {
            throw new DuplicateApplicationException("User already applied to this job");
        }

//        // check if user has uploaded resume
        if (user.getResumeUrl()==null){
            throw new BadRequestException("Please upload resume before applying");
        }

        // build and save
        Application application = Application.builder()
                .users(user)
                .job(job)
                .resumeUrl(user.getResumeUrl())
                .status(ApplicationStatus.PENDING)  // default
                .appliedAt(LocalDateTime.now())
                .build();

        return mapToDto(applicationRepo.save(application));
    }

    // 2. Get all applications by a user
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationsByUser(Long userId) {
        return applicationRepo.findByUsersId(userId).stream().map(this::mapToDto).toList();
    }

    // 3. Get all applications for a job (recruiter view)
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationsByJob(Long jobId, String recruiter_email) throws AccessDeniedException {

        //load recruiter
        Users recruiter = usersRepo.findByEmailWithCompany(recruiter_email).orElseThrow(() -> new ResourceNotFoundException(
                "Recruiter not found" + recruiter_email));

        // load job
        Job job = jobRepo.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job not found" + jobId));

        if (!job.getCompany().getId().equals(recruiter.getCompany().getId())) {
            throw new AccessDeniedException("You can only view your company job's");
        }
        return applicationRepo.findByJobId(jobId).stream().map(this::mapToDto).toList();
    }

    // 4. Update application status (recruiter shortlists/rejects)
    @Transactional
    public ApplicationResponseDto updateStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));

        application.setStatus(newStatus);
        return mapToDto(application);  // @Transactional makes this optional
    }

    // 5. Withdraw application
    public void withdraw(Long applicationId) {
        if (!applicationRepo.existsById(applicationId)) {
            throw new ResourceNotFoundException("Application not found: " + applicationId);
        }
        applicationRepo.deleteById(applicationId);
    }


    //dto mapper
    private ApplicationResponseDto mapToDto(Application app) {
        return ApplicationResponseDto.builder()
                .id(app.getId())
                .status(app.getStatus().name())
                .resumeUrl(app.getResumeUrl())
                .appliedAt(app.getAppliedAt())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .userId(app.getUsers().getId())
                .userName(app.getUsers().getFullname())
                .companyName(app.getJob().getCompany().getName())
                .build();
    }

}

