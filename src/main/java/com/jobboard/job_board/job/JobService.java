package com.jobboard.job_board.job;

import com.jobboard.job_board.Exception.ResourceNotFoundException;
import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.Users.Users;
import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import com.jobboard.job_board.job.dto.CursorResponse;
import com.jobboard.job_board.job.dto.JobPageResponse;
import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JobService {
    private final JobRepo jobRepo;
    private final CompanyRepo companyRepo;
    private final UserRepo userRepo;

    //fetch company from companyRepo, set on job
    //write method
    public JobResponseDTO createJob(JobRequestDTO request) {
        Company company =
                companyRepo.findById(request.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company id not " +
                        "found: " + request.getCompanyId()));
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location((request.getLocation()))
                .salary((request.getSalary()))
                .jobtype(request.getJobType())
                .company(company)
                .build();
        return jobResponseDTO(jobRepo.save(job));
    }

    //get job by id
    @Transactional(readOnly = true)
    public JobResponseDTO getHistoryByid(Long id) {
        Job job = jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));
        return jobResponseDTO(job);
    }

    //get all
    @Transactional(readOnly = true)
    public List<JobResponseDTO> getHistory() {
        return jobRepo.findAll().stream().map(this::jobResponseDTO).toList();
    }

//    by company id
    @Transactional(readOnly = true)
    public JobPageResponse getJobsByCompany(Long companyId,int page,int size,String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobPage = jobRepo.findByCompanyId(companyId,pageable);

        List<JobResponseDTO> jobs = jobPage.getContent()
                .stream()
                .map(this::jobResponseDTO)
                .toList();


        return JobPageResponse.builder()
                .jobs(jobs)
                .currentPage(jobPage.getNumber())
                .totalPages(jobPage.getTotalPages())
                .totalJobs(jobPage.getTotalElements())
                .isFirst(jobPage.isFirst())
                .isLast(jobPage.isLast())
                .pageSize(jobPage.getSize())
                .build();

    }

    //get all jobs
    @Transactional(readOnly = true)
    public JobPageResponse getAlljobsPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobPage = jobRepo.findAll(pageable);

        System.out.println("Content size: " + jobPage.getContent().size());
        System.out.println("First job: " + jobPage.getContent().get(0).getTitle());

        List<JobResponseDTO> jobs = jobPage.getContent()
                .stream()
                .map(this::jobResponseDTO)
                .toList();

        System.out.println("Mapped jobs size: " + jobs.size());

        return JobPageResponse.builder()
                .jobs(jobs)
                .currentPage(jobPage.getNumber())
                .totalPages(jobPage.getTotalPages())
                .totalJobs(jobPage.getTotalElements())
                .isFirst(jobPage.isFirst())
                .isLast(jobPage.isLast())
                .pageSize(jobPage.getSize())
                .build();
    }

    @Transactional(readOnly = true)
    public JobPageResponse getMyJobs(String recruiterEmail,int page, int size, String sortBy, String sortDir){
        // load recruiter
        Users recruiter = userRepo.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        // check recruiter has company
        if (recruiter.getCompany() == null) {
            throw new RuntimeException("Recruiter has no company assigned");
        }
        Sort sort = sortDir.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Job> jobPage = jobRepo.findByCompanyId(recruiter.getCompany().getId(),pageable);

            List<JobResponseDTO> jobs=jobPage.getContent()
                    .stream().map(this::jobResponseDTO).toList();

            return JobPageResponse.builder()
                    .jobs(jobs)
                    .currentPage(jobPage.getNumber())
                    .totalPages(jobPage.getTotalPages())
                    .totalJobs(jobPage.getTotalElements())
                    .isFirst(jobPage.isFirst())
                    .isLast(jobPage.isLast())
                    .pageSize(jobPage.getSize())
                    .build();

    }


    @Transactional(readOnly = true)
    public JobPageResponse searchJobs(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Job> jobPage = jobRepo.findByTitleContainingIgnoreCase(keyword, pageable);

        List<JobResponseDTO> jobs = jobPage.getContent()
                .stream()
                .map(this::jobResponseDTO)
                .toList();

        return JobPageResponse.builder()
                .jobs(jobs)
                .currentPage(jobPage.getNumber())
                .totalPages(jobPage.getTotalPages())
                .totalJobs(jobPage.getTotalElements())
                .isFirst(jobPage.isFirst())
                .isLast(jobPage.isLast())
                .pageSize(jobPage.getSize())
                .build();
    }

    @Transactional(readOnly = true)
    public JobPageResponse filterByLocation(String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Job> jobPage = jobRepo.findByLocation(location, pageable);

        List<JobResponseDTO> jobs = jobPage.getContent()
                .stream()
                .map(this::jobResponseDTO)
                .toList();

        return JobPageResponse.builder()
                .currentPage(jobPage.getNumber())
                .totalPages(jobPage.getTotalPages())
                .totalJobs(jobPage.getTotalElements())
                .isFirst(jobPage.isFirst())
                .isLast(jobPage.isLast())
                .pageSize(jobPage.getSize())
                .build();
    }

    @Transactional(readOnly = true)
    public CursorResponse getJobsCursor(Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<Job> jobs;

        if (cursor == null || cursor == 0) {
            jobs = jobRepo.findFirstPage(pageable);
        } else {
            jobs = jobRepo.findJobsByCursor(cursor, pageable);
        }
            List<JobResponseDTO> dtos = jobs.stream().map(this::jobResponseDTO).toList();
            Long nextCursor = jobs.isEmpty() ? null :
                    jobs.get(jobs.size() - 1).getId();

            boolean hasMore = nextCursor != null && jobRepo.existsByIdGreaterThan(nextCursor);

            return CursorResponse.builder()
                    .jobs(dtos)
                    .nextCursor(nextCursor)
                    .hasMore(hasMore)
                    .pageSize(size)
                    .build();


    }

    // just convert entity to dto
    public JobResponseDTO jobResponseDTO(Job j) {
        JobResponseDTO responseDTO = new JobResponseDTO();
        responseDTO.setId(j.getId());
        responseDTO.setTitle(j.getTitle());
        responseDTO.setDescription(j.getDescription());
        responseDTO.setLocation(j.getLocation());
        responseDTO.setSalary(j.getSalary());
        responseDTO.setJobType(j.getJobtype());
        responseDTO.setCompanyName(j.getCompany().getName());
        responseDTO.setCompanyId(j.getCompany().getId());
        return responseDTO;

    }

}
