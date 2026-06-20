package com.jobboard.job_board.job;

import com.jobboard.job_board.Users.UserRepo;
import com.jobboard.job_board.company.Company;
import com.jobboard.job_board.company.CompanyRepo;
import com.jobboard.job_board.job.dto.JobRequestDTO;
import com.jobboard.job_board.job.dto.JobResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepo jobRepo;

    @Mock
    private CompanyRepo companyRepo;

    @Mock
    private UserRepo userRepo;

    // inject the mocks into the service we want to test
    @InjectMocks
    private JobService jobService;

    //reusable objects for tests
    private Job job;
    private JobRequestDTO jobRequestDTO;
    private Company company;

    @BeforeEach
    void setup() {
        company = new Company();
        company.setId(1L);
        company.setName("TCS");
        company.setLocation("Pune");

        jobRequestDTO = new JobRequestDTO();
        jobRequestDTO.setTitle("Backend Developer");
        jobRequestDTO.setDescription("Spring Boot role");
        jobRequestDTO.setLocation("Pune");
        jobRequestDTO.setJobType("Full-time");
        jobRequestDTO.setSalary(new java.math.BigDecimal("800000"));
        jobRequestDTO.setCompanyId(1L);

        job = Job.builder()
                .id(1L)
                .title(jobRequestDTO.getTitle())
                .description(jobRequestDTO.getDescription())
                .location(jobRequestDTO.getLocation())
                .jobtype(jobRequestDTO.getJobType())
                .salary(jobRequestDTO.getSalary())
                .company(company)
                .build();
    }

    // write tests for createJob method
    @Test
    void testCreateJob_Success() {
        when(companyRepo.findById(1L)).thenReturn(Optional.of(company));
        when(jobRepo.save(any(Job.class))).thenReturn(job);

        JobResponseDTO response = jobService.createJob(jobRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(jobRequestDTO.getTitle());
        assertThat(response.getCompanyName()).isEqualTo(company.getName());

        verify(companyRepo, times(1)).findById(1L);
        verify(jobRepo, times(1)).save(any(Job.class));
    }

    // write tests for createJob method when company not found
    @Test
    void testCreateJob_CompanyNotFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());

        try {
            jobService.createJob(jobRequestDTO);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Company id not found: 1");
        }

        verify(companyRepo, times(1)).findById(1L);
        verify(jobRepo, times(0)).save(any(Job.class));
    }

    // write tests job not found by id
    @Test
    void createJob_CompanyNotFound() {
        when(companyRepo.findById(1L)).thenReturn(Optional.empty());

        try {
            jobService.createJob(jobRequestDTO);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Company id not found: 1");
        }

        verify(companyRepo, times(1)).findById(1L);
        verify(jobRepo, times(0)).save(any(Job.class));
    }

    // write tests for job not found by id
    @Test
    void testGetJobById_JobNotFound() {
        when(jobRepo.findById(1L)).thenReturn(Optional.empty());

        try {
            jobService.getHistoryByid(1L);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Id not found: 1");
        }

        verify(jobRepo, times(1)).findById(1L);
    }
}


