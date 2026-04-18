package com.jobboard.job_board.job;

import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class JobService {
    public final JobRepo jobRepo;
    private final CompanyRepo companyRepo;

    @Transactional
    //fetch company from companyRepo, set on job
    public JobResponseDTO createJob(JobRequestDTO request) {
        Company company =
                companyRepo.findById(request.getCompanyId()).orElseThrow(()->new RuntimeException("Company id not " +
                        "found: "+request.getCompanyId()));
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
    public JobResponseDTO getHistoryByid(Long id){
        Job job= jobRepo.findById(id).orElseThrow(()->new RuntimeException("Id not found: "+id));
        return jobResponseDTO(job);
    }

    //get all
    public List<JobResponseDTO> getHistory(){
       return jobRepo.findAll().stream().map(this::jobResponseDTO).toList();
    }

    //by company id
    public List<JobResponseDTO> getJobsByCompany(Long companyId){
        return jobRepo.findByCompanyId(companyId).stream().map(this::jobResponseDTO).toList();
    }

    //delete
    public void deleteJob(Long id){
        if (!jobRepo.existsById(id)){
            throw new RuntimeException("Job not found with id: "+id);
        }
        jobRepo.deleteById(id);
    }



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
